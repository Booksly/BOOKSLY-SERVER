package kyonggi.bookslyserver.domain.shop.service;

import kyonggi.bookslyserver.domain.event.entity.Event;
import kyonggi.bookslyserver.domain.event.entity.closeEvent.ClosingEvent;
import kyonggi.bookslyserver.domain.event.entity.timeEvent.TimeEvent;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import kyonggi.bookslyserver.domain.reservation.repository.ReservationScheduleRepository;
import kyonggi.bookslyserver.domain.reservation.repository.ReservationSettingRepository;
import kyonggi.bookslyserver.domain.review.repository.ReviewRepository;
import kyonggi.bookslyserver.domain.shop.dto.request.employee.EmployeeCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.request.employee.EmployeeUpdateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.request.employee.EmployeeWorkScheduleRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.response.employee.*;
import kyonggi.bookslyserver.domain.shop.dto.response.menu.ReadEmployeesMenusResponseDto;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Employee.EmployeeMenu;
import kyonggi.bookslyserver.domain.shop.entity.Employee.WorkSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.repository.*;
import kyonggi.bookslyserver.global.aws.s3.AmazonS3Manager;
import kyonggi.bookslyserver.global.common.uuid.Uuid;
import kyonggi.bookslyserver.global.common.uuid.UuidService;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static kyonggi.bookslyserver.global.error.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final EmployeeMenuRepository employeeMenuRepository;

    private final WorkScheduleRepository workScheduleRepository;

    private final ShopRepository shopRepository;

    private final MenuRepository menuRepository;

    private final ReservationScheduleRepository reservationScheduleRepository;

    private final ReservationSettingRepository reservationSettingRepository;

    private final UuidService uuidService;

    private final AmazonS3Manager amazonS3Manager;

    private final ReviewRepository reviewRepository;

    private final MenuCategoryRepository menuCategoryRepository;

    private final MenuImageRepository menuImageRepository;

    public ReadEmployeeWithReviewsWrapperResponseDto readEmployeesWithReviews(Long shopId, Boolean withReviews){
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new EntityNotFoundException(SHOP_NOT_FOUND));
        List<Employee> employees = employeeRepository.findByShop(shop);
        if (employees.isEmpty()) {
            throw new EntityNotFoundException(EMPLOYEE_NOT_CONFIG);
        }
        List<ReadEmployeeWithReviewsResponseDto> employeeDtos = employees.stream().map(employee -> {
            Integer reviewCount = withReviews ? reviewRepository.countReviewsByEmployeeId(employee.getId()) : null;
            return ReadEmployeeWithReviewsResponseDto.of(employee, withReviews, reviewCount);
        }).collect(Collectors.toList());

        return ReadEmployeeWithReviewsWrapperResponseDto.of(employeeDtos);
    }

    public ReadEmployeeResponseDto readEmployeeInfo(Long employeeId){
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EntityNotFoundException(EMPLOYEES_NOT_FOUND));

        List<EmployeeMenu> employeeMenus = employeeMenuRepository.findByEmployeeId(employeeId);
        List<Menu> menus = employeeMenus.stream()
                .map(EmployeeMenu::getMenu)
                .collect(Collectors.toList());

        List<ReadEmployeesMenusResponseDto> menusResponseDtos = menus.stream().collect(Collectors.groupingBy(Menu::getMenuCategory))
                .entrySet().stream()
                .map(entry -> ReadEmployeesMenusResponseDto.of(entry.getKey().getName(), entry.getValue()))
                .collect(Collectors.toList());

        return ReadEmployeeResponseDto.of(employee, menusResponseDtos);
    }


    private void validateDuplicatedEmployeeName(String name) {
        if (employeeRepository.existsByName(name))
            throw new ConflictException(ErrorCode.EMPLOYEE_NAME_DUPLICATE);
    }

    private void validateWorkSchedules(List<EmployeeWorkScheduleRequestDto> workSchedules) {
        if (workSchedules.size() != 7) throw new InvalidValueException(WORK_SCHEDULE_MUST_SEVEN_DAYS);
    }

    private String uploadEmployeeImageToS3(MultipartFile imageFile) {
        Uuid uuid = uuidService.createUuid();
        return amazonS3Manager.uploadFile(amazonS3Manager.generateEmployeeKeyName(uuid), imageFile);
    }

    private void createEmployeeMenu(Employee employee, List<Menu> menus) {
        menus.forEach(menu -> {
            EmployeeMenu employeeMenu = EmployeeMenu.builder()
                    .menu(menu)
                    .employee(employee).build();
            employeeMenuRepository.save(employeeMenu);
        });
    }

    private void assignAllMenusToEmployee(Shop shop, Employee employee) {
        List<Menu> menusByShop = menuRepository.findMenusByShop(shop);
        createEmployeeMenu(employee, menusByShop);
    }

    private void assignSpecificMenusToEmployee(EmployeeCreateRequestDto requestDto, Employee employee) {
        List<Menu> menus = requestDto.menus().stream()
                .map(menuId -> menuRepository.findById(menuId).orElseThrow(() -> new EntityNotFoundException(MENU_NOT_FOUND)))
                .collect(Collectors.toList());
        createEmployeeMenu(employee, menus);
    }

    public EmployeeCreateResponseDto join(Long shopId, Boolean assignAllMenus,EmployeeCreateRequestDto requestDto, MultipartFile profileImg){
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new EntityNotFoundException(SHOP_NOT_FOUND));
        validateDuplicatedEmployeeName(requestDto.name());
        validateWorkSchedules(requestDto.workSchedules());

        Employee employee = Employee.builder()
                .name(requestDto.name())
                .selfIntro(requestDto.description())
                .shop(shop)
                .schedulingCycle(reservationSettingRepository.findByShop(shop).orElseThrow(()-> new EntityNotFoundException(SETTING_NOT_FOUND)).getReservationCycle())
                .profileImgUri(profileImg != null ? uploadEmployeeImageToS3(profileImg) : null)
                .build();

        if (assignAllMenus) {
            assignAllMenusToEmployee(shop, employee);
        } else {
            assignSpecificMenusToEmployee(requestDto, employee);
        }

        //근무 스케쥴 생성
        requestDto.workSchedules().stream().forEach(workScheduleDto -> {
            WorkSchedule workSchedule = WorkSchedule.createEntity(workScheduleDto.dayOfWeek(), workScheduleDto.startTime(), workScheduleDto.endTime(), workScheduleDto.isDayOff(), employee);
            workScheduleRepository.save(workSchedule);
        });

        employeeRepository.save(employee);
        return new EmployeeCreateResponseDto(employee);
    }


    private void updateWorkSchedules(EmployeeUpdateRequestDto requestDto, Employee employee) {
        List<WorkSchedule> workSchedules = workScheduleRepository.findByEmployeeId(employee.getId());
        Map<DayOfWeek, WorkSchedule> workScheduleMap = workSchedules.stream().collect(Collectors.toMap(WorkSchedule::getDayOfWeek, Function.identity()));
        requestDto.workSchedules().stream().forEach(workScheduleDto -> {
            WorkSchedule workSchedule = workScheduleMap.get(workScheduleDto.dayOfWeek());
            workSchedule.updateWorkSchedule(workScheduleDto.startTime(), workScheduleDto.endTime(), workScheduleDto.isDayOff());
        });
    }

    private void updateAllEmployeeMenu(Shop shop, Employee employee) {
        List<Menu> menusByShop = menuRepository.findMenusByShopWithCategories(shop);
        List<EmployeeMenu> employeeMenus = employeeMenuRepository.findByEmployeeId(employee.getId());

        // 기존 직원 메뉴 삭제
        if (!employeeMenus.isEmpty()) {
            employeeMenuRepository.deleteAll(employeeMenus);
            employee.deleteAllEmployeeMenu();
            employeeMenus.clear();
        }

        for (Menu menu : menusByShop) {
            employeeMenus.add(EmployeeMenu.createEntity(employee, menu));
        }
    }

    private void updateEmployeeMenu(EmployeeUpdateRequestDto requestDto, Employee employee, Shop shop) {
        // 기존 직원 담당 메뉴
        List<EmployeeMenu> existedEmployeeMenus = employeeMenuRepository.findByEmployeeId(employee.getId());
        Set<Long> existedMenuIds = existedEmployeeMenus.stream()
                .map(em -> em.getMenu().getId())
                .collect(Collectors.toSet());

        // 새롭게 요청된 직원 담당 메뉴
        List<Menu> shopMenus = shop.getMenus();
        List<Menu> newMenus = requestDto.menuIds().stream()
                .map(menuId -> shopMenus.stream()
                        .filter(menu -> menu.getId().equals(menuId))
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException(MENU_NOT_FOUND)))
                .collect(Collectors.toList());

        Set<Long> newMenuIds = newMenus.stream()
                .map(Menu::getId)
                .collect(Collectors.toSet());

        // 새 메뉴에는 있고 기존 메뉴에는 없으면 해당 직원 담당 메뉴 생성
        newMenuIds.stream()
                .filter(menuId -> !existedMenuIds.contains(menuId))
                .forEach(menuId -> {
                    Menu menu = newMenus.stream()
                            .filter(m -> m.getId().equals(menuId))
                            .findFirst()
                            .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MENU_NOT_FOUND));
                    EmployeeMenu employeeMenu = EmployeeMenu.createEntity(employee, menu);
                    employeeMenuRepository.save(employeeMenu);
                });

        // 새 메뉴에는 없고 기존 메뉴에만 있으면 해당 직원 담당 메뉴 삭제
        existedMenuIds.stream()
                .filter(menuId -> !newMenuIds.contains(menuId))
                .forEach(menuId -> {
                    EmployeeMenu employeeMenu = existedEmployeeMenus.stream()
                            .filter(em -> em.getMenu().getId().equals(menuId))
                            .findFirst()
                            .orElseThrow(() -> new EntityNotFoundException(ErrorCode.EMPLOYEE_MENU_NOT_FOUND));
                    employee.deleteEmployeeMenu(employeeMenu);
                    employeeMenuRepository.delete(employeeMenu);
                });
    }

    private void updateProfileImg(MultipartFile profileImg, Employee employee) {
        if (employee.getProfileImgUri() != null) amazonS3Manager.deleteFileFromUrl(employee.getProfileImgUri());
        String employeePictureUrl = uploadEmployeeImageToS3(profileImg);
        employee.updateProfileImgUrl(employeePictureUrl);
    }

    public UpdateEmployeeResponseDto update(Long shopId, Long employeeId, Boolean assignAllMenus, EmployeeUpdateRequestDto requestDto, MultipartFile profileImage){
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new EntityNotFoundException(SHOP_NOT_FOUND));
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EntityNotFoundException(EMPLOYEE_NOT_FOUND));

        if (requestDto.workSchedules() != null) {
            validateWorkSchedules(requestDto.workSchedules());
            updateWorkSchedules(requestDto, employee);
        }

        if (requestDto.menuIds() != null) {
            if (assignAllMenus) updateAllEmployeeMenu(shop, employee);
            else updateEmployeeMenu(requestDto, employee, shop);
        }

        if (profileImage != null) {
            updateProfileImg(profileImage, employee);
        }

        if (requestDto.name() != null || !requestDto.name().isEmpty()) {
            validateDuplicatedEmployeeName(requestDto.name());
            employee.updateName(requestDto.name());
        }

        if (requestDto.description() != null) {
            employee.updateSelfIntro(requestDto.description());
        }

        employeeRepository.save(employee);
        menuRepository.findMenusByIdsWithImage(employee.getEmployeeMenus().stream().map(employeeMenu -> employeeMenu.getId()).collect(Collectors.toList()));
        return UpdateEmployeeResponseDto.of(employee);
    }


    @Transactional
    public EmployeeDeleteResponseDto delete(Long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(EMPLOYEE_NOT_FOUND));


        Shop shop = employee.getShop();
        shop.getEmployees().remove(employee);
        employeeRepository.deleteById(id);
        return new EmployeeDeleteResponseDto(id);
    }

    public GetCalendarDatesResponseDto getCalendarDates(Long shopId, Long employeeId) {

        shopRepository.findById(shopId).orElseThrow(() -> new EntityNotFoundException(SHOP_NOT_FOUND));
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EntityNotFoundException(EMPLOYEE_NOT_FOUND));

        if (employee.getShop().getId() != shopId) throw new ForbiddenException();

        LocalDate currentDate = LocalDate.now();
        List<WorkSchedule> workSchedules = employee.getWorkSchedules();
        int schedulingCycle = employee.getSchedulingCycle();
        LocalDate plusDate = currentDate .plusDays(schedulingCycle-1);

        List<LocalDate> workdays = new ArrayList<>();
        List<LocalDate> holidays = new ArrayList<>();

        Map<DayOfWeek,Boolean> workInfo = workSchedules.stream().collect(Collectors.toMap(WorkSchedule::getDayOfWeek,WorkSchedule::isDayOff));
        while (!currentDate.isAfter(plusDate)) {
            Boolean isDayOff = workInfo.get(currentDate .getDayOfWeek());
            if (isDayOff) holidays.add(currentDate );
            else workdays.add(currentDate );

            currentDate = currentDate.plusDays(1);
        }

        return GetCalendarDatesResponseDto.of(employee.getId(), workdays, holidays);
    }

    public Employee findEmployeeById(Long employeeId) {
        return employeeRepository
                .findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(EMPLOYEE_NOT_FOUND));

    }

    public void validateBelongShop(Long employeeId, Long shopId) {
        if (!employeeRepository.existsByIdAndShopId(employeeId, shopId))
            throw new InvalidValueException(EMPLOYEE_NOT_BELONG_SHOP);
    }


    private void validateReservationScheduleBelongEmployee(Employee employee, ReservationSchedule reservationSchedule) {
        if (reservationSchedule.getEmployee().getId() != employee.getId()) {
            throw new InvalidValueException(EMPLOYEE_NOT_OWN_RESERVATION_SCHEDULE);
        }
    }

    @Transactional(readOnly = true)
    public CheckEventStatusResponseDto checkEventStatus(Long shopId, Long employeeId, Long reservationScheduleId) {
        Employee employee = findEmployeeById(employeeId);
        validateBelongShop(employeeId,shopId);

        ReservationSchedule reservationSchedule = reservationScheduleRepository.findById(reservationScheduleId).orElseThrow(() -> new EntityNotFoundException(RESERVATION_SCHEDULE_NOT_FOUND));
        validateReservationScheduleBelongEmployee(employee, reservationSchedule);
        boolean hasEvent = !reservationSchedule.isClosed() &&
                (reservationSchedule.isClosingEvent() || reservationSchedule.getTimeEventSchedule() != null);

        return CheckEventStatusResponseDto.of(hasEvent);
    }

    public List<Menu> findEmployeeClosingEventMenus(Employee employee, ClosingEvent closingEvent) {
        List<Menu> menus = closingEvent.getClosingEventMenus().stream()
                .map(closingEventMenu -> closingEventMenu.getMenu()).collect(Collectors.toList());
        return employeeMenuRepository.findByMenusAndEmployee(menus, employee);
    }

    public List<Menu> findEmployeeTimeEventMenus(Employee employee, TimeEvent timeEvent) {
        List<Menu> menus = timeEvent.getTimeEventMenus().stream()
                .map(timeEventMenu -> timeEventMenu.getMenu()).collect(Collectors.toList());

        return employeeMenuRepository.findByMenusAndEmployee(menus, employee);
    }

    public GetEventMenusResponseDto getEventMenus(Long shopId, Long employeeId, Long reservationScheduleId) {
        Employee employee = findEmployeeById(employeeId);
        validateBelongShop(employeeId,shopId);

        ReservationSchedule reservationSchedule = reservationScheduleRepository.findById(reservationScheduleId).orElseThrow(() -> new EntityNotFoundException(RESERVATION_SCHEDULE_NOT_FOUND));
        validateReservationScheduleBelongEmployee(employee, reservationSchedule);

        Map<Event, List<Menu>> eventMenus = new HashMap<>();
        if (!reservationSchedule.isClosed() && reservationSchedule.isClosingEvent()) {
            ClosingEvent closingEvent = reservationSchedule.getClosingEvent();
            List<Menu> employeeClosingEventMenus = findEmployeeClosingEventMenus(employee, closingEvent);
            eventMenus.put(closingEvent, employeeClosingEventMenus);
        }

        if (reservationSchedule.getTimeEventSchedule() != null) {
            TimeEvent timeEvent = reservationSchedule.getTimeEventSchedule().getTimeEvent();
            List<Menu> employeeTimeEventMenus = findEmployeeTimeEventMenus(employee, timeEvent);
            eventMenus.put(timeEvent, employeeTimeEventMenus);
        }

        return GetEventMenusResponseDto.of(eventMenus);
    }

    public ReadEmployeeNamesWithImageWrapperResponseDto readEmployeeNamesWithImages(Long shopId, Boolean withImages) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new EntityNotFoundException(SHOP_NOT_FOUND));
        List<Employee> employees = employeeRepository.findByShop(shop);
        if (employees.isEmpty()) {
            throw new EntityNotFoundException(EMPLOYEE_NOT_CONFIG);
        }
        return ReadEmployeeNamesWithImageWrapperResponseDto.of(employees, withImages);
    }
}

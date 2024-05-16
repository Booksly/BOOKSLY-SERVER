package kyonggi.bookslyserver.domain.shop.service;

import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.shop.dto.request.employee.EmployeeCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.request.employee.EmployeeWorkScheduleDto;
import kyonggi.bookslyserver.domain.shop.dto.response.employee.*;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Employee.EmployeeMenu;
import kyonggi.bookslyserver.domain.shop.entity.Employee.WorkSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.repository.*;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.BusinessException;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import kyonggi.bookslyserver.global.error.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static kyonggi.bookslyserver.global.error.ErrorCode.EMPLOYEE_NOT_FOUND;
import static kyonggi.bookslyserver.global.error.ErrorCode.SHOP_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final EmployeeMenuRepository employeeMenuRepository;

    private final WorkScheduleRepository workScheduleRepository;

    private final ShopRepository shopRepository;

    private final MenuRepository menuRepository;

    public List<EmployeeReadDto> readEmployee(Long id){
        Optional<Shop> shop = shopRepository.findById(id);

        if(!shop.isPresent()){
            throw new EntityNotFoundException();
        }

        List<EmployeeReadDto> employeeReadDtos = new ArrayList<>();

        if(shop.get().getEmployees() != null) {
            for (Employee employee : shop.get().getEmployees()) {
                EmployeeReadDto employeeReadDto = new EmployeeReadDto(employee);
                employeeReadDtos.add(employeeReadDto);
            }
        }

        return employeeReadDtos;
    }


    public EmployeeReadOneDto readOneEmployee(Long id){
        Optional<Employee> employee = employeeRepository.findById(id);
        if(!employee.isPresent()){
            throw new EntityNotFoundException(EMPLOYEE_NOT_FOUND);
        }
        List<EmployeeMenu> employeeMenus = employeeMenuRepository.findByEmployeeId(id);


        return new EmployeeReadOneDto(employee.get(), employeeMenus);
    }

    @Transactional
    public EmployeeCreateResponseDto join(Long id, EmployeeCreateRequestDto requestDto){
        Optional<Shop> shop = shopRepository.findById(id);
        if(!shop.isPresent()){
            throw new EntityNotFoundException();
        }

        Employee employee = Employee.createEntity(shop.get(), requestDto);

        employeeRepository.save(employee);

        shop.get().getEmployees().add(employee);

        if(requestDto.menus() != null){
            for(String menuName : requestDto.menus()){
                Menu menu = menuRepository.findByMenuName(menuName);
                if(menu == null){
                    throw new BusinessException(ErrorCode.MENU_NOT_FOUND);
                }
                EmployeeMenu employeeMenu = employee.addMenu(employee, menu);
            }
        }

        if(requestDto.workSchedules() != null) {
            for (EmployeeWorkScheduleDto employeeWorkScheduleDto : requestDto.workSchedules()) {
                WorkSchedule workSchedule = WorkSchedule.createEntity(employee, employeeWorkScheduleDto);
                employee.getWorkSchedules().add(workSchedule);
            }
        }

        return new EmployeeCreateResponseDto(employee);
    }

    @Transactional
    public Long update(Long id, EmployeeCreateRequestDto requestDto){
        Optional<Employee> employee = employeeRepository.findById(id);
        if(!employee.isPresent()){
            throw new EntityNotFoundException();
        }

        List<EmployeeMenu> employeeMenuList = employeeMenuRepository.findByEmployeeId(id);
        List<WorkSchedule> workScheduleList = workScheduleRepository.findByEmployeeId(id);

        for(EmployeeMenu employeeMenu : employeeMenuList){
            employeeMenuRepository.delete(employeeMenu);
        }

        for(WorkSchedule workSchedule : workScheduleList){
            workScheduleRepository.delete(workSchedule);
        }
        if(requestDto.menus() != null){
            for(String menuName : requestDto.menus()){
                Menu menu = menuRepository.findByMenuName(menuName);
                employee.get().addMenu(employee.get(), menu);
            }
        }

        for(EmployeeWorkScheduleDto employeeWorkScheduleDto : requestDto.workSchedules()){
            WorkSchedule workSchedule = WorkSchedule.createEntity(employee.get(), employeeWorkScheduleDto);
            employee.get().getWorkSchedules().add(workSchedule);
        }

        employee.get().update(requestDto);

        return employee.get().getId();
    }

    @Transactional
    public EmployeeDeleteResponseDto delete(Long id){
        Optional<Employee> employee = employeeRepository.findById(id);

        if(!employee.isPresent()){
            throw new EntityNotFoundException();
        }

        Shop shop = employee.get().getShop();
        shop.getEmployees().remove(employee.get());
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
}

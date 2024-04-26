package kyonggi.bookslyserver.domain.shop.service;

import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.shop.dto.request.EmployeeCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.request.EmployeeWorkScheduleDto;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Employee.EmployeeMenu;
import kyonggi.bookslyserver.domain.shop.entity.Employee.WorkSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.repository.EmployeeMenuRepository;
import kyonggi.bookslyserver.domain.shop.repository.EmployeeRepository;
import kyonggi.bookslyserver.domain.shop.repository.MenuRepository;
import kyonggi.bookslyserver.domain.shop.repository.ShopRepository;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final EmployeeMenuRepository employeeMenuRepository;

    private final ShopRepository shopRepository;

    private final MenuRepository menuRepository;

    @Transactional
    public Long join(Long id, EmployeeCreateRequestDto requestDto){
        Optional<Shop> shop = shopRepository.findById(id);
        if(!shop.isPresent()){
            throw new EntityNotFoundException();
        }

        Employee employee = Employee.createEntity(shop.get(), requestDto);

        employeeRepository.save(employee);

        if(!requestDto.menus().isEmpty()){
            for(String menuName : requestDto.menus()){
                Menu menu = menuRepository.findByMenuName(menuName);
                EmployeeMenu employeeMenu = employee.addMenu(employee, menu);
            }
        }

        for(EmployeeWorkScheduleDto employeeWorkScheduleDto : requestDto.workSchedules()){
            WorkSchedule workSchedule = WorkSchedule.createEntity(employee, employeeWorkScheduleDto);
            employee.getWorkSchedules().add(workSchedule);
        }
        return employee.getId();
    }

}

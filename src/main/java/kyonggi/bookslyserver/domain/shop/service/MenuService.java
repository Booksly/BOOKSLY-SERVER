package kyonggi.bookslyserver.domain.shop.service;

import jakarta.transaction.Transactional;

import kyonggi.bookslyserver.domain.event.entity.closeEvent.ClosingEventMenu;
import kyonggi.bookslyserver.domain.shop.dto.request.menu.MenuCategoryCreateDto;
import kyonggi.bookslyserver.domain.shop.dto.request.menu.MenuCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.response.menu.*;

import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Employee.EmployeeMenu;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import kyonggi.bookslyserver.domain.shop.entity.Menu.MenuCategory;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.repository.*;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.BusinessException;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    private final MenuImageRepository menuImageRepository;

    private final MenuCategoryRepository menuCategoryRepository;

    private final ShopRepository shopRepository;

    private final EmployeeRepository employeeRepository;


    public MenuReadOneDto readOneMenu(Long id){
        Optional<Menu> menu = menuRepository.findById(id);
        if(!menu.isPresent()){
            throw new EntityNotFoundException(ErrorCode.MENU_NOT_FOUND);
        }

        return new MenuReadOneDto(menu.get());
    }
  
    public List<MenuReadDto> readMenu(Long id){
        Optional<Shop> shop = shopRepository.findById(id);
        if(!shop.isPresent()){
            throw new EntityNotFoundException();
        }
        List<MenuReadDto> menuReadDtos = new ArrayList<>();

        for(Menu menu : shop.get().getMenus()){
            MenuReadDto menuDto = new MenuReadDto(menu);
            menuReadDtos.add(menuDto);
        }
        return menuReadDtos;
    }

    public List<EventRegisterMenuNamesDto> readMenuNames(Long id){
        Optional<Employee> employee = employeeRepository.findById(id);
        List<Menu> menus = new ArrayList<>();
        List<Menu> deletemenus = new ArrayList<>();
        if(!employee.isPresent()){
            throw new EntityNotFoundException(ErrorCode.EMPLOYEE_NOT_FOUND);
        }

        for(EmployeeMenu employeeMenu : employee.get().getEmployeeMenus()){
            menus.add(employeeMenu.getMenu());
        }

        for(Menu menu : menus){
            for(ClosingEventMenu closingEventMenu : menu.getClosingEventMenus()){
                if(closingEventMenu.getClosingEvent().getEmployee() == employee.get()){
                    deletemenus.add(menu);
                }
            }
        }

        for(Menu menu : deletemenus){
            menus.remove(menu);
        }

        List<EventRegisterMenuNamesDto> result = menus.stream().map(menu -> new EventRegisterMenuNamesDto(menu)).collect(Collectors.toList());

        return result;
    }

    @Transactional
    public MenuCreateResponseDto create(Long id, MenuCreateRequestDto requestDto){
        Optional<Shop> shop = shopRepository.findById(id);
        if(!shop.isPresent()){
            throw new EntityNotFoundException();
        }
        Menu menu = Menu.createEntity(shop.get(), requestDto);

        if(!menuCategoryRepository.existsByName(requestDto.menuCategory())){
            throw new BusinessException(ErrorCode.MENUCATEGORY_NOT_FOUND);
        }
        else{
            MenuCategory menuCategory = menuCategoryRepository.findByName(requestDto.menuCategory());
            shop.get().getMenus().add(menu);
            menuCategory.addMenu(menu);
            menu.addImg(menu.getMenuImages());

            menuRepository.save(menu);
        }

        return MenuCreateResponseDto.builder().id(menu.getId()).build();

    }

    @Transactional
    public MenuUpdateResponseDto update(Long id, MenuCreateRequestDto requestDto){
        Optional<Menu> menu = menuRepository.findById(id);
        if(!menu.isPresent()){
            throw new EntityNotFoundException();
        }

        for(int i = menu.get().getMenuImages().size() - 1; i >= 0; i--){
            menuImageRepository.delete(menu.get().getMenuImages().get(i).getMenuImgUri());
            menu.get().getMenuImages().remove(i);
        }


        List<String> images = menu.get().update(requestDto);

        return MenuUpdateResponseDto
                .builder()
                .menuName(menu.get().getMenuName())
                .price(menu.get().getPrice())
                .description(menu.get().getDescription())
                .menuCategory(menu.get().getMenuCategory().getName())
                .images(images).build();
    }

    @Transactional
    public void delete(Long id){
        Optional<Menu> menu = menuRepository.findById(id);
        if(!menu.isPresent()){
            throw new EntityNotFoundException();
        }

        if(menu.get().getMenuCategory().getMenus().size() == 1){
            menuCategoryRepository.delete(menu.get().getMenuCategory());
        }
        else{
            menuRepository.delete(menu.get());
        }
    }

    public List<MenuCategoryReadDto> readMenuCategory(Long id){
        Optional<Shop> shop = shopRepository.findById(id);
        if(!shop.isPresent()){
            throw new EntityNotFoundException();
        }

        List<MenuCategoryReadDto> dtos = new ArrayList<>();

        if(shop.get().getMenuCategories() != null){
            for(MenuCategory menuCategory : shop.get().getMenuCategories()){
                dtos.add(new MenuCategoryReadDto(menuCategory));
            }
        }
        return dtos;
    }

    @Transactional
    public MenuCategoryCreateResponseDto createCategory(Long id, MenuCategoryCreateDto requestDto){
        Optional<Shop> shop = shopRepository.findById(id);
        if(!shop.isPresent()){
            throw new EntityNotFoundException();
        }
        if(!menuCategoryRepository.existsByName(requestDto.categoryName())) {
            MenuCategory menuCategory = MenuCategory.createEntity(requestDto, shop.get());
            shop.get().getMenuCategory(menuCategory);
            menuCategoryRepository.save(menuCategory);
            return new MenuCategoryCreateResponseDto(menuCategory);
        }
        else{
            throw new BusinessException(ErrorCode.MENUCATEGORY_ALREADY_EXIST);
        }
    }

    @Transactional
    public MenuCategoryCreateDto updateCategory(Long id, MenuCategoryCreateDto requestDto){
        Optional<MenuCategory> menuCategory = menuCategoryRepository.findById(id);
        if(!menuCategory.isPresent()){
            throw new EntityNotFoundException();
        }
        menuCategory.get().setName(requestDto.categoryName());
        return MenuCategoryCreateDto.builder().categoryName(menuCategory.get().getName()).build();
    }

    @Transactional
    public void deleteCategory(Long id){
        Optional<MenuCategory> menuCategory = menuCategoryRepository.findById(id);
        if(!menuCategory.isPresent()){
            throw new EntityNotFoundException();
        }
        if(!menuCategory.get().getMenus().isEmpty()){
            throw new BusinessException(ErrorCode.MENU_ALREADY_EXIST);
        }
        else{
            menuCategory.get().getShop().getMenuCategories().remove(menuCategory.get());
            menuCategoryRepository.delete(menuCategory.get().getId());
        }
    }

}

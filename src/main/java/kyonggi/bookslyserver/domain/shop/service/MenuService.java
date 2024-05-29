package kyonggi.bookslyserver.domain.shop.service;

import jakarta.transaction.Transactional;

import kyonggi.bookslyserver.domain.shop.dto.request.menu.MenuCategoryCreateDto;
import kyonggi.bookslyserver.domain.shop.dto.request.menu.MenuCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.response.menu.*;

import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Employee.EmployeeMenu;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import kyonggi.bookslyserver.domain.shop.entity.Menu.MenuCategory;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.repository.*;
import kyonggi.bookslyserver.global.aws.s3.AmazonS3Manager;
import kyonggi.bookslyserver.global.common.uuid.Uuid;
import kyonggi.bookslyserver.global.common.uuid.UuidService;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.BusinessException;
import kyonggi.bookslyserver.global.error.exception.ConflictException;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static kyonggi.bookslyserver.global.error.ErrorCode.MENUCATEGORY_ALREADY_EXIST;
import static kyonggi.bookslyserver.global.error.ErrorCode.SHOP_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    private final MenuImageRepository menuImageRepository;

    private final MenuCategoryRepository menuCategoryRepository;

    private final ShopRepository shopRepository;

    private final EmployeeRepository employeeRepository;

    private final AmazonS3Manager amazonS3Manager;

    private final UuidService uuidService;


    public MenuReadOneDto readOneMenu(Long id){
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.MENU_NOT_FOUND));
        return new MenuReadOneDto(menu);
    }
  
    public ReadMenusByCategoryWrapperResponseDto readMenu(Long id){
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(SHOP_NOT_FOUND));
        List<MenuCategory> menuCategories = shop.getMenuCategories();
        return ReadMenusByCategoryWrapperResponseDto.of(menuCategories);
    }


    public List<EventRegisterEmployeeMenuDto> readMenuNamesEventRegister(Long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.EMPLOYEE_NOT_FOUND));
        List<Menu> menus = new ArrayList<>();
        Set<EventRegisterEmployeeMenuDto> eventRegisterEmployeeMenuDtos = new HashSet<>();

        for(EmployeeMenu employeeMenu : employee.getEmployeeMenus()){
            menus.add(employeeMenu.getMenu());
        }

        for(Menu menu : menus){
            eventRegisterEmployeeMenuDtos.add(new EventRegisterEmployeeMenuDto(menu));
        }

        List<EventRegisterEmployeeMenuDto> result = new ArrayList<>(eventRegisterEmployeeMenuDtos);

        for(Menu menu : menus){
                for(EventRegisterEmployeeMenuDto e : result){
                    if(menu.getMenuCategory().getName().equals(e.getMenuCategoryName())){
                        e.getMenu().add(new EventRegisterEmployeeMenuDto.MenuDto(menu));
                    }
                }
        }
        return result;
    }

    @Transactional
    public MenuCreateResponseDto create(Long id, MenuCreateRequestDto requestDto){
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(SHOP_NOT_FOUND));

        if(!menuCategoryRepository.existsByNameAndShopOwner(requestDto.menuCategory())){
            throw new BusinessException(ErrorCode.MENUCATEGORY_NOT_FOUND);
        }
        else{
            MenuCategory menuCategory = menuCategoryRepository.findByName(requestDto.menuCategory());
            shop.getMenus().add(menu);
            menuCategory.addMenu(menu);
            menu.addImg(menu.getMenuImages());

            menuRepository.save(menu);
        }
        Menu menu = Menu.createEntity(shop, requestDto);


        return MenuCreateResponseDto.builder().id(menu.getId()).build();

    }

    private String uploadMenuImgToS3(MenuCreateRequestDto requestDto) {
        Uuid uuid = uuidService.createUuid();
        String pictureUrl = amazonS3Manager.uploadFile(
                amazonS3Manager.generateMenuKeyName(uuid, requestDto.menuImg().getOriginalFilename()), requestDto.menuImg());
        return pictureUrl;
    }

    public MenuUpdateResponseDto update(Long id, MenuCreateRequestDto requestDto){
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.MENU_NOT_FOUND));
        if (menu.getMenuImage() != null) amazonS3Manager.deleteFileFromUrl(menu.getMenuImage().getMenuImgUri());

        String menuPictureUrl = uploadMenuImgToS3(requestDto);
        menu.update(requestDto, menuPictureUrl);

        return MenuUpdateResponseDto.builder()
                .menuName(menu.getMenuName())
                .price(menu.getPrice())
                .description(menu.getDescription())
                .menuCategory(menu.getMenuCategory().getName())
                .imgUrl(menuPictureUrl).build();
    }

    @Transactional
    public MenuDeleteResponseDto delete(Long id){
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.MENU_NOT_FOUND));


        if(menu.getMenuCategory().getMenus().size() == 1){
            menuCategoryRepository.delete(menu.getMenuCategory());
        }
        else{
            menu.getShop().getMenus().remove(menu);
            menuRepository.delete(menu.getId());
        }
        return new MenuDeleteResponseDto(menu);
    }

    public List<MenuCategoryReadDto> readMenuCategory(Long id){
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(SHOP_NOT_FOUND));


        List<MenuCategoryReadDto> dtos = new ArrayList<>();

        if(shop.getMenuCategories() != null){
            for(MenuCategory menuCategory : shop.getMenuCategories()){
                dtos.add(new MenuCategoryReadDto(menuCategory));
            }
        }
        return dtos;
    }

    @Transactional
    public MenuCategoryCreateResponseDto createCategory(Long ownerId, Long shopId, MenuCategoryCreateDto requestDto){
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new EntityNotFoundException(SHOP_NOT_FOUND));

        //가게 주인은 동일한 이름의 메뉴 카테고리 생성 불가
        if (menuCategoryRepository.existsByNameAndShopOwner(requestDto.categoryName(), ownerId)) {
            throw new ConflictException(MENUCATEGORY_ALREADY_EXIST);
        }

        MenuCategory menuCategory = MenuCategory.createEntity(requestDto, shop);
        shop.addMenuCategory(menuCategory);
        menuCategoryRepository.save(menuCategory);
        return new MenuCategoryCreateResponseDto(menuCategory);

    }

    @Transactional
    public MenuCategoryCreateDto updateCategory(Long id, MenuCategoryCreateDto requestDto){
        MenuCategory menuCategory = menuCategoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.MENUCATEGORY_NOT_FOUND));

        if(menuCategoryRepository.existsByNameAndShopOwner(requestDto.categoryName())){
            throw new BusinessException(MENUCATEGORY_ALREADY_EXIST);
        }
        menuCategory.setName(requestDto.categoryName());
        return MenuCategoryCreateDto.builder().categoryName(menuCategory.getName()).build();
    }

    @Transactional
    public MenuCategoryDeleteResponseDto deleteCategory(Long id){
        MenuCategory menuCategory = menuCategoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.MENUCATEGORY_NOT_FOUND));

        if(!menuCategory.getMenus().isEmpty()){
            throw new BusinessException(ErrorCode.MENU_ALREADY_EXIST);
        }
        else{
            menuCategory.getShop().getMenuCategories().remove(menuCategory);
            menuCategoryRepository.delete(menuCategory.getId());
        }
        return new MenuCategoryDeleteResponseDto(menuCategory);
    }

}

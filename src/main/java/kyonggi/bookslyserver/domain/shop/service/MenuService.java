package kyonggi.bookslyserver.domain.shop.service;

import jakarta.transaction.Transactional;

import kyonggi.bookslyserver.domain.shop.dto.request.menu.MenuCategoryCreateDto;
import kyonggi.bookslyserver.domain.shop.dto.request.menu.MenuCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.response.menu.*;

import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Employee.EmployeeMenu;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import kyonggi.bookslyserver.domain.shop.entity.Menu.MenuCategory;
import kyonggi.bookslyserver.domain.shop.entity.Menu.MenuImage;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.repository.*;
import kyonggi.bookslyserver.global.aws.s3.AmazonS3Manager;
import kyonggi.bookslyserver.global.common.uuid.Uuid;
import kyonggi.bookslyserver.global.common.uuid.UuidService;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.BusinessException;
import kyonggi.bookslyserver.global.error.exception.ConflictException;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import kyonggi.bookslyserver.global.error.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static kyonggi.bookslyserver.global.error.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    private final MenuImageRepository menuImageRepository;

    private final ShopService shopService;

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

    public MenuCreateResponseDto create(Long ownerId, Long shopId, MenuCreateRequestDto requestDto){
        Shop shop = shopService.findShop(ownerId, shopId);

        String categoryName = requestDto.menuCategory();
        MenuCategory menuCategory = menuCategoryRepository.findByNameAndShopOwner(categoryName, ownerId).orElseThrow(() -> new EntityNotFoundException(MENUCATEGORY_NOT_FOUND));

        if (menuRepository.existsNameInCategory(requestDto.menuName(), categoryName))
            throw new ConflictException(MENU_NAME_ALREADY_EXIST);

        Menu menu = Menu.createEntity(requestDto, shop);

        if (requestDto.menuImg() != null) {
            String menuPictureUrl = uploadMenuImgToS3(requestDto);
            MenuImage image = MenuImage.builder().menuImgUri(menuPictureUrl).menu(menu).build();
            menuImageRepository.save(image);
            menu.addImg(image);
        }

        menuCategory.addMenu(menu);
        shop.getMenus().add(menu);

        menuRepository.save(menu);
        return MenuCreateResponseDto.of(menu);

    }

    private String uploadMenuImgToS3(MenuCreateRequestDto requestDto) {
        Uuid uuid = uuidService.createUuid();
        String pictureUrl = amazonS3Manager.uploadFile(
                amazonS3Manager.generateMenuKeyName(uuid, requestDto.menuImg().getOriginalFilename()), requestDto.menuImg());
        return pictureUrl;
    }

    public MenuUpdateResponseDto update(Long id, MenuCreateRequestDto requestDto){ //DTO 따로 생성
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
    public MenuCategoryCreateDto updateCategory(Long ownerId, Long categoryId, MenuCategoryCreateDto requestDto){
        MenuCategory menuCategory = menuCategoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException(MENUCATEGORY_NOT_FOUND));

        if(menuCategoryRepository.existsByNameAndShopOwner(requestDto.categoryName(), ownerId)){
            throw new ConflictException(MENUCATEGORY_ALREADY_EXIST);
        }

        menuCategory.updateName(requestDto.categoryName());
        return MenuCategoryCreateDto.builder().categoryName(menuCategory.getName()).build();
    }

    @Transactional
    public MenuCategoryDeleteResponseDto deleteCategory(Long categoryId){
        MenuCategory menuCategory = menuCategoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException(MENUCATEGORY_NOT_FOUND));

        if(!menuCategory.getMenus().isEmpty()){
            throw new InvalidValueException(ErrorCode.CATEGORY_HAS_EXISTING_MENU);
        }

        menuCategory.getShop().getMenuCategories().remove(menuCategory);
        menuCategoryRepository.delete(menuCategory.getId());

        return new MenuCategoryDeleteResponseDto(menuCategory);
    }

}

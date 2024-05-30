package kyonggi.bookslyserver.domain.shop.service;

import jakarta.transaction.Transactional;

import kyonggi.bookslyserver.domain.shop.dto.request.menu.MenuCategoryCreateDto;
import kyonggi.bookslyserver.domain.shop.dto.request.menu.MenuCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.request.menu.UpdateMenuRequestDto;
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
import kyonggi.bookslyserver.global.error.exception.ConflictException;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import kyonggi.bookslyserver.global.error.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

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


    public MenuReadOneDto readMenu(Long id){
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.MENU_NOT_FOUND));
        return new MenuReadOneDto(menu);
    }
  
    public ReadMenusByCategoryWrapperResponseDto readShopMenus(Long id){
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(SHOP_NOT_FOUND));
        List<MenuCategory> menuCategories = shop.getMenuCategories();
        return ReadMenusByCategoryWrapperResponseDto.of(menuCategories);
    }


    public ReadEmployeesMenusWrapperResponseDto readEmployeesMenus(List<Long> employeeIds){

        List<Employee> employees = employeeRepository.findAllById(employeeIds);
        if (employees.size() != employeeIds.size()) {
            throw new EntityNotFoundException(ErrorCode.EMPLOYEE_NOT_FOUND);
        }

        // 각 직원의 메뉴를 uniqueMenus에 추가하여 메뉴 중복 제거
        Set<Menu> uniqueMenus = employees.stream()
                .flatMap(employee ->  employee.getEmployeeMenus().stream())
                .map(EmployeeMenu::getMenu)
                .collect(Collectors.toSet());


        // 카테고리별로 메뉴를 분류하고 DTO 변환
        List<ReadEmployeesMenusResponseDto> readEmployeesMenusResponseDtos = uniqueMenus.stream()
                .collect(Collectors.groupingBy(menu -> menu.getMenuCategory().getName()))
                .entrySet().stream()
                .map(entry -> ReadEmployeesMenusResponseDto.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return ReadEmployeesMenusWrapperResponseDto.of(readEmployeesMenusResponseDtos);
    }

    public MenuCreateResponseDto create(Long ownerId, Long shopId, MenuCreateRequestDto requestDto){
        Shop shop = shopService.findShop(ownerId, shopId);

        String categoryName = requestDto.menuCategory();
        MenuCategory menuCategory = menuCategoryRepository.findByNameAndShopOwner(categoryName, ownerId).orElseThrow(() -> new EntityNotFoundException(MENUCATEGORY_NOT_FOUND));

        if (menuRepository.existsNameInCategory(requestDto.menuName(), categoryName))
            throw new ConflictException(MENU_NAME_ALREADY_EXIST);

        Menu menu = Menu.createEntity(requestDto, shop);

        if (requestDto.menuImg() != null) {
            String menuPictureUrl = uploadMenuImgToS3(requestDto.menuImg());
            MenuImage image = MenuImage.builder().menuImgUri(menuPictureUrl).menu(menu).build();
            menuImageRepository.save(image);
            menu.addImg(image);
        }

        menuCategory.addMenu(menu);
        shop.getMenus().add(menu);

        menuRepository.save(menu);
        return MenuCreateResponseDto.of(menu);

    }

    private String uploadMenuImgToS3(MultipartFile image) {
        Uuid uuid = uuidService.createUuid();
        String pictureUrl = amazonS3Manager.uploadFile(
                amazonS3Manager.generateMenuKeyName(uuid, image.getOriginalFilename()), image);
        return pictureUrl;
    }

    public MenuUpdateResponseDto update(Long id, UpdateMenuRequestDto requestDto){
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.MENU_NOT_FOUND));

        // 이미지 업데이트 로직
        if (requestDto.menuImg() != null && !requestDto.menuImg().isEmpty()) {
            // 기존 이미지 삭제
            if (menu.getMenuImage() != null) {
                amazonS3Manager.deleteFileFromUrl(menu.getMenuImage().getMenuImgUri());
            }
            // 새 이미지 업로드
            String menuPictureUrl = uploadMenuImgToS3(requestDto.menuImg());
            MenuImage image = MenuImage.builder().menuImgUri(menuPictureUrl).menu(menu).build();
            menuImageRepository.save(image);
            menu.setMenuImage(image);  // 메뉴에 이미지 설정
        }

        // 메뉴 업데이트
        menu.update(requestDto.menuName(), requestDto.price(), requestDto.description(), requestDto.menuCategory());

        return MenuUpdateResponseDto.builder()
                .menuName(menu.getMenuName())
                .price(menu.getPrice())
                .description(menu.getDescription())
                .menuCategory(menu.getMenuCategory().getName())
                .imgUrl(menu.getMenuImage() != null ? menu.getMenuImage().getMenuImgUri() : null)
                .build();
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

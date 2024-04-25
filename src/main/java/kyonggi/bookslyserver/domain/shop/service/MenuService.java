package kyonggi.bookslyserver.domain.shop.service;

import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.shop.dto.request.MenuCategoryCreateDto;
import kyonggi.bookslyserver.domain.shop.dto.request.MenuCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.response.menu.MenuCreateResponseDto;
import kyonggi.bookslyserver.domain.shop.dto.response.menu.MenuUpdateResponseDto;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import kyonggi.bookslyserver.domain.shop.entity.Menu.MenuCategory;
import kyonggi.bookslyserver.domain.shop.entity.Menu.MenuImage;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.repository.MenuCategoryRepository;
import kyonggi.bookslyserver.domain.shop.repository.MenuImageRepository;
import kyonggi.bookslyserver.domain.shop.repository.MenuRepository;
import kyonggi.bookslyserver.domain.shop.repository.ShopRepository;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.BusinessException;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    private final MenuImageRepository menuImageRepository;

    private final MenuCategoryRepository menuCategoryRepository;

    private final ShopRepository shopRepository;

    @Transactional
    public MenuCreateResponseDto create(Long id, MenuCreateRequestDto requestDto){
        Optional<Shop> shop = shopRepository.findById(id);
        if(!shop.isPresent()){
            throw new EntityNotFoundException();
        }
        Menu menu = Menu.createEntity(shop.get(), requestDto);
        shop.get().getMenus().add(menu);
        MenuCategory menuCategory = MenuCategory.createEntity(requestDto);
        menuCategory.addMenu(menu);
        menu.addImg(menu.getMenuImages());
        menuCategoryRepository.save(menuCategory);
        menuRepository.save(menu);
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

    @Transactional
    public Long createCategory(Long id, MenuCategoryCreateDto requestDto){
        Optional<Shop> shop = shopRepository.findById(id);
        if(!shop.isPresent()){
            throw new EntityNotFoundException();
        }
        MenuCategory menuCategory = MenuCategory.createEntity(requestDto, shop.get());
        shop.get().getMenuCategory(menuCategory);
        menuCategoryRepository.save(menuCategory);
        return menuCategory.getId();
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

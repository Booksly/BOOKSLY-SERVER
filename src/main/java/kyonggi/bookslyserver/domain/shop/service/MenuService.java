package kyonggi.bookslyserver.domain.shop.service;

import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.shop.dto.request.MenuCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.response.MenuCreateResponseDto;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import kyonggi.bookslyserver.domain.shop.entity.Menu.MenuImage;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.repository.MenuRepository;
import kyonggi.bookslyserver.domain.shop.repository.ShopRepository;
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

    private final ShopRepository shopRepository;

    @Transactional
    public MenuCreateResponseDto create(Long id, MenuCreateRequestDto requestDto){
        List<MenuImage> menuImageList = new ArrayList<>();
        for(String img : requestDto.menuImgUri()){
            menuImageList.add(MenuImage.builder().menuImgUri(img).build());
        }
        Menu menu = Menu.createEntity(requestDto, menuImageList);
        Optional<Shop> shop = shopRepository.findById(id);
        if(!shop.isPresent()){
            throw new EntityNotFoundException();
        }
        menu.register(shop.get());
        menuRepository.save(menu);

        return MenuCreateResponseDto.builder().id(menu.getId()).build();
    }

}

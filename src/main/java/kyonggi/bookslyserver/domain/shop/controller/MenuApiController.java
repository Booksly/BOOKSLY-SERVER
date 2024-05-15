package kyonggi.bookslyserver.domain.shop.controller;



import com.sun.net.httpserver.Authenticator;
import kyonggi.bookslyserver.domain.shop.dto.request.MenuCategoryCreateDto;
import kyonggi.bookslyserver.domain.shop.dto.request.MenuCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.response.menu.MenuCategoryCreateResponseDto;
import kyonggi.bookslyserver.domain.shop.dto.response.menu.MenuCategoryReadDto;
import kyonggi.bookslyserver.domain.shop.dto.response.menu.MenuCreateResponseDto;
import kyonggi.bookslyserver.domain.shop.dto.response.menu.MenuReadOneDto;
import kyonggi.bookslyserver.domain.shop.dto.response.menu.MenuReadDto;
import kyonggi.bookslyserver.domain.shop.dto.response.menu.MenuUpdateResponseDto;
import kyonggi.bookslyserver.domain.shop.service.MenuService;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MenuApiController {

    private final MenuService menuService;


    @GetMapping("/api/shop/{menuId}/menu")
    public ResponseEntity<SuccessResponse<?>> readOneMenu(@PathVariable("menuId") Long id){
        MenuReadOneDto result = menuService.readOneMenu(id);
        return SuccessResponse.ok(result);
    }


    @GetMapping("/api/shop/{shopId}/menus")
    public ResponseEntity<SuccessResponse<?>> readMenu(@PathVariable("shopId") Long id){
        List<MenuReadDto> result = menuService.readMenu(id);
        return SuccessResponse.ok(result);
    }

    @PostMapping("/api/menu/{shopId}")
    public ResponseEntity<SuccessResponse<?>> createMenu(@PathVariable("shopId") Long id, @RequestBody @Validated MenuCreateRequestDto requestDto){
        MenuCreateResponseDto result = menuService.create(id, requestDto);
        return SuccessResponse.ok(result);
    }


    @PutMapping("/api/menu/{menuId}")
    public ResponseEntity<SuccessResponse<?>> updateMenu(@PathVariable("menuId") Long id, @RequestBody @Validated MenuCreateRequestDto requestDto){
        MenuUpdateResponseDto result = menuService.update(id, requestDto);
        return SuccessResponse.ok(result);
    }

    @DeleteMapping("/api/menu/{menuId}")
    public void delete(@PathVariable("menuId") Long id){
        menuService.delete(id);
    }

    @GetMapping("/api/shop/{shopId}/categories")
    public ResponseEntity<SuccessResponse<?>> readCategory(@PathVariable("shopId") Long id){
        List<MenuCategoryReadDto> result = menuService.readMenuCategory(id);
        return SuccessResponse.ok(result);
    }

    @PostMapping("/api/menuCategory/{shopId}")
    public ResponseEntity<SuccessResponse<?>> createMenuCategory(@PathVariable("shopId") Long id, @RequestBody @Validated MenuCategoryCreateDto requestDto){
        MenuCategoryCreateResponseDto result = menuService.createCategory(id, requestDto);
        return SuccessResponse.ok(result);
    }

    @PutMapping("/api/menuCategory/{categoryId}")
    public ResponseEntity<SuccessResponse<?>> updateMenuCategory(@PathVariable("categoryId") Long id, @RequestBody @Validated MenuCategoryCreateDto requestDto){
        MenuCategoryCreateDto result = menuService.updateCategory(id, requestDto);
        return SuccessResponse.ok(result);
    }

    @DeleteMapping("/api/menuCategory/{categoryId}")
    public void deleteMenuCategory(@PathVariable("categoryId") Long id){
        menuService.deleteCategory(id);
    }
}

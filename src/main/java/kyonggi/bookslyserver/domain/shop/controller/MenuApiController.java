package kyonggi.bookslyserver.domain.shop.controller;



import kyonggi.bookslyserver.domain.shop.dto.request.menu.MenuCategoryCreateDto;
import kyonggi.bookslyserver.domain.shop.dto.request.menu.MenuCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.response.menu.*;
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



    @GetMapping("/api/shops/menus/{menuId}")
    public ResponseEntity<SuccessResponse<?>> readOneMenu(@PathVariable("menuId") Long id){
        MenuReadOneDto result = menuService.readOneMenu(id);
        return SuccessResponse.ok(result);
    }


    @GetMapping("/api/shops/{shopId}/menus")
    public ResponseEntity<SuccessResponse<?>> readMenu(@PathVariable("shopId") Long id){
        ReadMenusByCategoryWrapperResponseDto responseDto = menuService.readMenu(id);
        return SuccessResponse.ok(responseDto);
    }

    @GetMapping("api/shops/employees/{employeeId}/menus/EventRegistration")
    public ResponseEntity<SuccessResponse<?>> readMenuNamesTimeEventRegister(@PathVariable("employeeId") Long id){
        List<EventRegisterEmployeeMenuDto> result = menuService.readMenuNamesEventRegister(id);
        return SuccessResponse.ok(result);
    }

    @PostMapping("/api/shops/{shopId}/menus")
    public ResponseEntity<SuccessResponse<?>> createMenu(@PathVariable("shopId") Long id, @RequestBody @Validated MenuCreateRequestDto requestDto){
        MenuCreateResponseDto result = menuService.create(id, requestDto);
        return SuccessResponse.ok(result);
    }


    @PutMapping("/api/shops/menus/{menuId}")
    public ResponseEntity<SuccessResponse<?>> updateMenu(@PathVariable("menuId") Long id, @RequestBody @Validated MenuCreateRequestDto requestDto){
        MenuUpdateResponseDto result = menuService.update(id, requestDto);
        return SuccessResponse.ok(result);
    }

    @DeleteMapping("/api/shops/menus/{menuId}")
    public ResponseEntity<SuccessResponse<?>> delete(@PathVariable("menuId") Long id){

        MenuDeleteResponseDto result = menuService.delete(id);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/api/shops/{shopId}/categories")
    public ResponseEntity<SuccessResponse<?>> readCategory(@PathVariable("shopId") Long id){
        List<MenuCategoryReadDto> result = menuService.readMenuCategory(id);
        return SuccessResponse.ok(result);
    }

    @PostMapping("/api/shops/{shopId}/menuCategories")
    public ResponseEntity<SuccessResponse<?>> createMenuCategory(@PathVariable("shopId") Long id, @RequestBody @Validated MenuCategoryCreateDto requestDto){
        MenuCategoryCreateResponseDto result = menuService.createCategory(id, requestDto);
        return SuccessResponse.ok(result);
    }

    @PutMapping("/api/shops/menuCategories/{categoryId}")
    public ResponseEntity<SuccessResponse<?>> updateMenuCategory(@PathVariable("categoryId") Long id, @RequestBody @Validated MenuCategoryCreateDto requestDto){
        MenuCategoryCreateDto result = menuService.updateCategory(id, requestDto);
        return SuccessResponse.ok(result);
    }

    @DeleteMapping("/api/shops/menuCategories/{categoryId}")
    public ResponseEntity<SuccessResponse<?>> deleteMenuCategory(@PathVariable("categoryId") Long id){
        MenuCategoryDeleteResponseDto result = menuService.deleteCategory(id);
        return SuccessResponse.ok(result);
    }
}

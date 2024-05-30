package kyonggi.bookslyserver.domain.shop.controller;



import kyonggi.bookslyserver.domain.shop.dto.request.menu.MenuCategoryCreateDto;
import kyonggi.bookslyserver.domain.shop.dto.request.menu.MenuCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.request.menu.UpdateMenuRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.response.menu.*;
import kyonggi.bookslyserver.domain.shop.service.MenuService;
import kyonggi.bookslyserver.global.auth.principal.shopOwner.OwnerId;
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



    @GetMapping("/api/shops/{shopId}/menus")
    public ResponseEntity<SuccessResponse<?>> readShopMenus(@PathVariable("shopId") Long id){
        ReadMenusByCategoryWrapperResponseDto responseDto = menuService.readShopMenus(id);
        return SuccessResponse.ok(responseDto);
    }

    @GetMapping("/api/shops/employees/menus")
    public ResponseEntity<SuccessResponse<?>> readEmployeesMenus(@RequestParam("employeeIds") List<Long> employeeIds){
        ReadEmployeesMenusWrapperResponseDto responseDto = menuService.readEmployeesMenus(employeeIds);
        return SuccessResponse.ok(responseDto);
    }

    @PostMapping("/api/shops/{shopId}/menus")
    public ResponseEntity<SuccessResponse<?>> createMenu(@OwnerId Long ownerId, @PathVariable("shopId") Long shopId, @RequestBody @Validated MenuCreateRequestDto requestDto){
        MenuCreateResponseDto result = menuService.create(ownerId, shopId, requestDto);
        return SuccessResponse.created(result);
    }

    @GetMapping("/api/shops/menus/{menuId}")
    public ResponseEntity<SuccessResponse<?>> readMenu(@PathVariable("menuId") Long id){
        MenuReadOneDto result = menuService.readMenu(id);
        return SuccessResponse.ok(result);
    }


    @PutMapping("/api/shops/menus/{menuId}")
    public ResponseEntity<SuccessResponse<?>> updateMenu(@PathVariable("menuId") Long id, @RequestBody @Validated UpdateMenuRequestDto requestDto){
        MenuUpdateResponseDto result = menuService.update(id, requestDto);
        return SuccessResponse.ok(result);
    }

    @DeleteMapping("/api/shops/menus/{menuId}")
    public ResponseEntity<SuccessResponse<?>> deleteMenu(@PathVariable("menuId") Long id){
        MenuDeleteResponseDto result = menuService.delete(id);
        return SuccessResponse.ok(result);
    }

    @PostMapping("/api/shops/{shopId}/categories")
    public ResponseEntity<SuccessResponse<?>> createMenuCategory(@OwnerId Long ownerId, @PathVariable("shopId") Long shopId, @RequestBody @Validated MenuCategoryCreateDto requestDto){
        MenuCategoryCreateResponseDto result = menuService.createCategory(ownerId, shopId, requestDto);
        return SuccessResponse.created(result);
    }

    @GetMapping("/api/shops/{shopId}/categories")
    public ResponseEntity<SuccessResponse<?>> readMenuCategory(@PathVariable("shopId") Long id){
        List<MenuCategoryReadDto> result = menuService.readMenuCategory(id);
        return SuccessResponse.ok(result);
    }

    @PutMapping("/api/shops/categories/{categoryId}")
    public ResponseEntity<SuccessResponse<?>> updateMenuCategory(@OwnerId Long ownerId, @PathVariable("categoryId") Long categoryId, @RequestBody @Validated MenuCategoryCreateDto requestDto){
        MenuCategoryCreateDto result = menuService.updateCategory(ownerId, categoryId, requestDto);
        return SuccessResponse.ok(result);
    }

    @DeleteMapping("/api/shops/categories/{categoryId}")
    public ResponseEntity<SuccessResponse<?>> deleteMenuCategory(@PathVariable("categoryId") Long id){
        MenuCategoryDeleteResponseDto result = menuService.deleteCategory(id);
        return SuccessResponse.ok(result);
    }
}

package kyonggi.bookslyserver.domain.shop.service;


import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.shop.dto.request.shop.ShopCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.response.menu.MenuReadDto;
import kyonggi.bookslyserver.domain.shop.dto.response.shop.*;
import kyonggi.bookslyserver.domain.shop.entity.BusinessSchedule.BusinessSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.entity.Shop.ShopImage;
import kyonggi.bookslyserver.domain.shop.repository.BusinessScheduleRepository;
import kyonggi.bookslyserver.domain.shop.repository.CategoryRepository;
import kyonggi.bookslyserver.domain.shop.repository.ShopImageRepository;
import kyonggi.bookslyserver.domain.shop.repository.ShopRepository;
import kyonggi.bookslyserver.domain.user.entity.ShopOwner;
import kyonggi.bookslyserver.domain.user.repository.ShopOwnerRepository;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.BusinessException;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import kyonggi.bookslyserver.global.error.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kyonggi.bookslyserver.global.error.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;

    private final BusinessScheduleRepository businessScheduleRepository;

    private final ShopImageRepository shopImageRepository;

    private final ShopOwnerRepository shopOwnerRepository;

    private final CategoryRepository categoryRepository;

    private final String ALL_REGION = "전체";



    public ShopUserReadOneDto findOne(Long id){
        Optional<Shop> shop = shopRepository.findById(id);
        if(!shop.isPresent()){
            throw new EntityNotFoundException(ErrorCode.SHOP_NOT_FOUND);
        }

        String name = shop.get().getName();
        String description = shop.get().getIntroduction();
        String detailAddress = shop.get().getDetailAddress();
        String phoneNumber = shop.get().getPhoneNumber();

        List<BusinessSchedule> businessSchedules = shop.get().getBusinessSchedules();
        List<BusinessScheduleDto> businessScheduleDtos = businessSchedules.stream().map(businessSchedule -> new BusinessScheduleDto(businessSchedule)).collect(Collectors.toList());

        List<Menu> menus = shop.get().getMenus();
        List<MenuReadDto> menuReadDtos = menus.stream().map(menu -> new MenuReadDto(menu)).collect(Collectors.toList());

        List<Employee> employeeList = shop.get().getEmployees();
        List<EmployeeUserResponseDto> employees = employeeList.stream().map(employee -> new EmployeeUserResponseDto(employee)).collect(Collectors.toList());

        int visit = shop.get().getTotalVisitors() + 1;
        shop.get().setTotalVisitors(visit);

        return ShopUserReadOneDto
                .builder()
                .Name(name)
                .description(description)
                .detailAddress(detailAddress)
                .phoneNumber(phoneNumber)
                .businessSchedules(businessScheduleDtos)
                .menus(menuReadDtos)
                .employees(employees)
                .address(new AddressDto(shop.get().getAddress()))
                .build();
    }

    public List<ShopFilterDto> readTopShops(Pageable pageable){
        Page<Shop> shopPage = shopRepository.findAll(pageable);
        List<ShopFilterDto> result = shopPage.stream().map(shop -> new ShopFilterDto(shop)).collect(Collectors.toList());
        return result;
    }

    @Transactional
    public ShopRegisterDto join(Long ownerId, ShopCreateRequestDto requestDto) {

        if(shopRepository.existsByName(requestDto.getName())){
            throw new BusinessException(SHOP_NAME_ALREADY_EXIST);
        }
        Shop shop = Shop.createShop(requestDto);
        shopRepository.save(shop);
        List<BusinessSchedule> businessScheduleList = requestDto.getBusinessScheduleList();
        for(BusinessSchedule businessSchedule : businessScheduleList){
            shop.getBusinessSchedule(businessSchedule);
        }

        List<ShopImage> shopImages = requestDto.getShopImageList();
        for(ShopImage shopImage : shopImages){
            shop.getShopImage(shopImage);
        }

        Optional<ShopOwner> owner = shopOwnerRepository.findById(ownerId);
        shop.getShopOwner(owner);

        return new ShopRegisterDto(shop);
    }

    @Transactional
    public ShopCreateResponseDto update(Long id, ShopCreateRequestDto requestDto){
        Optional<Shop> shop = shopRepository.findById(id);

        if(!shop.isPresent()){
            throw new EntityNotFoundException();
        }
        shop.get().update(shop.get(), requestDto);
        return new ShopCreateResponseDto(shop.get());
    }

    @Transactional
    public ShopDeleteResponseDto delete(Long id){
        Optional<Shop> shop = shopRepository.findById(id);

        if(!shop.isPresent()){
            throw new EntityNotFoundException();
        }

        ShopOwner owner = shop.get().getShopOwner();
        owner.deleteShop(shop.get());
        //shopRepository.delete(shop.get());
        shopRepository.deleteById(id);
        return new ShopDeleteResponseDto(id);
    }

    //가게 이름 조회(가게 주인)
    public List<ReadShopNamesDto> readShopNames(Long id){
        ShopOwner owner = shopOwnerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        List<Shop> shopList = shopRepository.findByShopOwnerId(id);

        List<ReadShopNamesDto> result = new ArrayList<>();

        for(Shop shop : shopList){
            result.add(new ReadShopNamesDto(shop));
        }
        return result;
    }

    public ShopOwnerDetailReadOneDto readOne(Long id){
        Optional<Shop> shop = shopRepository.findById(id);
        if(!shop.isPresent()){
            throw new EntityNotFoundException();
        }
        List<BusinessScheduleDto> businessScheduleDtos = new ArrayList<>();

        for(BusinessSchedule businessSchedule : shop.get().getBusinessSchedules()){
            businessScheduleDtos.add(new BusinessScheduleDto(businessSchedule));
        }


        return new ShopOwnerDetailReadOneDto(shop.get(), businessScheduleDtos);
    }




    public Shop findShop(Long ownerId, Long shopId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new EntityNotFoundException(SHOP_NOT_FOUND));
        checkShopOwner(ownerId, shop);
        return shop;
    }

    private void checkShopOwner(Long ownerId, Shop shop) {
        if (shop.getShopOwner().getId() != ownerId) {
            throw new InvalidValueException(NOT_OWNER_OF_SHOP);
        }
    }

    private List<Shop> filterShopsByAddress(List<Shop> shops, String region) {
        String[] address = region.split(" ");

        if (address[2].equals(ALL_REGION)) {
            shops = shopRepository.findShopsByOneAndTwoAddress(address[0], address[1], shops);
        }else {
            shops = shopRepository.findShopsByAddress(address[0], address[1], address[2], shops);
        }
        return shops;
    }

    public List<Shop> getRegionFilteredShops(List<String> regions, List<Shop> shops) {
        List<Shop> filteredShops = new ArrayList<>();
        regions.stream().forEach(region ->
                filteredShops.addAll(filterShopsByAddress(shops, region)));
        return filteredShops;
    }

    private void validateCategoriesExist(List<Long> categoryIds) {
        List<Long> existingCategoryIds = categoryRepository.findByCategoryIds(categoryIds);
        if (existingCategoryIds.size() != categoryIds.size()) {
            throw new InvalidValueException(ErrorCode.CATEGORY_NOT_FOUNT);
        }
    }

    public List<Shop> getCategoryFilteredShops(List<Long> categories, List<Shop> shops) {
        validateCategoriesExist(categories);
        return shopRepository.findByCategoryIds(categories,shops);
    }


}

package kyonggi.bookslyserver.domain.shop.service;


import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.shop.dto.request.shop.ShopCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.response.shop.*;
import kyonggi.bookslyserver.domain.shop.entity.BusinessSchedule.BusinessSchedule;
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

import java.time.LocalDate;
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



    public ShopUserReadOneDto getShopProfileDetails(Long shopId){
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new EntityNotFoundException(SHOP_NOT_FOUND));

        shop.setTotalVisitors(shop.getTotalVisitors() + 1);

        return ShopUserReadOneDto.builder()
                .Name(shop.getName())
                .rating(shop.getRatingByReview())
                .description(shop.getIntroduction())
                .detailAddress(shop.getDetailAddress())
                .phoneNumber(shop.getPhoneNumber())
                .businessSchedules(
                        shop.getBusinessSchedules().stream()
                                .map(BusinessScheduleDto::new)
                                .toList()
                )
                .address(new AddressDto(shop.getAddress()))
                .build();
    }
    public ShopOwnerDetailReadOneDto getShopProfileDetailsOwner(Long id){
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(SHOP_NOT_FOUND));

        List<BusinessScheduleDto> businessScheduleDtos = new ArrayList<>();

        for(BusinessSchedule businessSchedule : shop.getBusinessSchedules()){
            businessScheduleDtos.add(new BusinessScheduleDto(businessSchedule));
        }


        return new ShopOwnerDetailReadOneDto(shop, businessScheduleDtos);
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

        Shop shop=shopRepository.save(Shop.createShop(requestDto));

        for(BusinessSchedule businessSchedule : requestDto.getBusinessScheduleList()){
            shop.setBusinessSchedule(businessSchedule);
        }

        for(ShopImage shopImage : requestDto.getShopImageList()){
            shop.setShopImage(shopImage);
        }

        shop.setShopOwner(shopOwnerRepository.findById(ownerId).orElseThrow(()-> new EntityNotFoundException(SHOP_OWNER_NOT_EXIST)));
        return new ShopRegisterDto(shop);
    }

    @Transactional
    public ShopCreateResponseDto update(Long id, ShopCreateRequestDto requestDto){
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(SHOP_NOT_FOUND));


        shop.update(shop, requestDto);
        return new ShopCreateResponseDto(shop);
    }

    @Transactional
    public ShopDeleteResponseDto delete(Long id){
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(SHOP_NOT_FOUND));


        ShopOwner owner = shop.getShopOwner();
        owner.deleteShop(shop);
        shopRepository.deleteById(id);
        return new ShopDeleteResponseDto(id);
    }

    //가게 이름 조회(가게 주인)
    public List<ReadShopNamesDto> readShopNames(Long ownerId){
        if (!shopImageRepository.existsById(ownerId)) throw new EntityNotFoundException(SHOP_OWNER_NOT_EXIST);

        return  shopRepository.findByShopOwnerId(ownerId).stream()
                .map(ReadShopNamesDto::new)
                .collect(Collectors.toList());
    }

    public ShopOwnerMainReadOneDto readMain(Long id){
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(SHOP_NOT_FOUND));

        return new ShopOwnerMainReadOneDto(shop);
    }



    public List<NewShopFilterDto> readNewShops(Pageable pageable){
        Page<Shop> shopPage = shopRepository.findNewShops(pageable, LocalDate.now(), LocalDate.now().minusMonths(3));
        List<NewShopFilterDto> result = shopPage.stream().map(shop -> new NewShopFilterDto(shop)).collect(Collectors.toList());
        return result;
    }


    public Shop findShop(Long shopId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new EntityNotFoundException(SHOP_NOT_FOUND));
        return shop;
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
            throw new InvalidValueException(ErrorCode.CATEGORY_NOT_FOUND);
        }
    }

    public List<Shop> getCategoryFilteredShops(List<Long> categories, List<Shop> shops) {
        validateCategoriesExist(categories);
        return shopRepository.findByCategoryIds(categories,shops);
    }


}

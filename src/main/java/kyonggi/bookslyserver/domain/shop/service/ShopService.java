package kyonggi.bookslyserver.domain.shop.service;


import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.shop.constant.CategoryName;
import kyonggi.bookslyserver.domain.shop.dto.request.ShopCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.response.ShopCreateResponseDto;
import kyonggi.bookslyserver.domain.shop.entity.BusinessSchedule.BusinessSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Address;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Category;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.entity.Shop.ShopImage;
import kyonggi.bookslyserver.domain.shop.repository.BusinessScheduleRepository;
import kyonggi.bookslyserver.domain.shop.repository.ShopImageRepository;
import kyonggi.bookslyserver.domain.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;

    private final BusinessScheduleRepository businessScheduleRepository;

    private final ShopImageRepository shopImageRepository;
    @Transactional
    public ShopCreateResponseDto join(ShopCreateRequestDto requestDto) {
        Category category;

        if (requestDto.getCategory().equals("HAIR")) {
            category = Category.builder().categoryName(CategoryName.HAIR).build();
        } else if (requestDto.getCategory().equals("NAIL")) {
            category = Category.builder().categoryName(CategoryName.NAIL).build();
        } else if (requestDto.getCategory().equals("MAKEUP")) {
            category = Category.builder().categoryName(CategoryName.MAKEUP).build();
        } else if (requestDto.getCategory().equals("EYEBROW")) {
            category = Category.builder().categoryName(CategoryName.EYEBROW).build();
        } else if (requestDto.getCategory().equals("MASSAGE")) {
            category = Category.builder().categoryName(CategoryName.MASSAGE).build();
        } else if (requestDto.getCategory().equals("WAXING")) {
            category = Category.builder().categoryName(CategoryName.WAXING).build();
        } else {
            category = Category.builder().categoryName(CategoryName.ETC).build();
        }

        Shop shop = Shop.builder()
                .name(requestDto.getName())
                .phoneNumber(requestDto.getPhoneNumber())
                .businessNumber(requestDto.getBusinessNumber())
                .category(category)
                .address(Address.builder().firstAddress(requestDto.getFirstAddress()).secondAddress(requestDto.getSecondAddress()).thirdAddress(requestDto.getThirdAddress()).build())
                .detailAddress(requestDto.getDetailAddress())
                .kakaoUrl(requestDto.getKakaoUrl())
                .instagramUrl(requestDto.getInstagramUrl())
                .introduction(requestDto.getIntroduction())
                .businessSchedules(new ArrayList<>())
                .shopImages(new ArrayList<>())
                .timeUnit(requestDto.getTimeUnit())
                .build();

        shopRepository.save(shop);

        List<BusinessSchedule> businessScheduleList = requestDto.getBusinessScheduleList();
        for(BusinessSchedule businessSchedule : businessScheduleList){
            shop.getBusinessSchedule(businessSchedule);
            businessScheduleRepository.save(businessSchedule);
        }

        List<ShopImage> shopImages = requestDto.getShopImageList();
        for(ShopImage shopImage : shopImages){
            System.out.println("================================================================");
            System.out.println(shopImage.isRepresentative());
            System.out.println("================================================================");
            shop.getShopImage(shopImage);
            shopImageRepository.save(shopImage);
        }


        return new ShopCreateResponseDto(shop);
    }
}

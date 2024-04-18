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
import kyonggi.bookslyserver.domain.user.entity.ShopOwner;
import kyonggi.bookslyserver.domain.user.repository.ShopOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;

    private final BusinessScheduleRepository businessScheduleRepository;

    private final ShopImageRepository shopImageRepository;

    private final ShopOwnerRepository shopOwnerRepository;
    @Transactional
    public ShopCreateResponseDto join(Long ownerId, ShopCreateRequestDto requestDto) {

        Shop shop = Shop.createShop(requestDto);

        shopRepository.save(shop);

        List<BusinessSchedule> businessScheduleList = requestDto.getBusinessScheduleList();
        for(BusinessSchedule businessSchedule : businessScheduleList){
            shop.getBusinessSchedule(businessSchedule);
            businessScheduleRepository.save(businessSchedule);
        }

        List<ShopImage> shopImages = requestDto.getShopImageList();
        for(ShopImage shopImage : shopImages){
            shop.getShopImage(shopImage);
            shopImageRepository.save(shopImage);
        }

        Optional<ShopOwner> owner = shopOwnerRepository.findById(ownerId);
        shop.getShopOwner(owner);


        return new ShopCreateResponseDto(shop);
    }

    @Transactional
    public ShopCreateResponseDto update(Long id, ShopCreateRequestDto requestDto){
        Optional<Shop> shop = shopRepository.findById(id);

        if(!shop.isPresent()){
            throw new NullPointerException("해당 id의 shop 엔티티가 없음");
        }
        shop.get().update(shop.get(), requestDto);
        return new ShopCreateResponseDto(shop.get());
    }

    @Transactional
    public void delete(Long id){
        Optional<Shop> shop = shopRepository.findById(id);
        ShopOwner owner = shop.get().getShopOwner();
        owner.deleteShop(shop.get());
        shopRepository.deleteById(id);
    }





}

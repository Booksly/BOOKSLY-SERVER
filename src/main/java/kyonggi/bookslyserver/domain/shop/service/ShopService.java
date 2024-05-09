package kyonggi.bookslyserver.domain.shop.service;


import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.shop.dto.BusinessScheduleDto;
import kyonggi.bookslyserver.domain.shop.dto.request.ShopCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.response.shop.ShopCreateResponseDto;
import kyonggi.bookslyserver.domain.shop.dto.response.shop.ShopDeleteResponseDto;
import kyonggi.bookslyserver.domain.shop.dto.response.shop.ShopOwnerDetailReadOneDto;
import kyonggi.bookslyserver.domain.shop.dto.response.shop.ShopRegisterDto;
import kyonggi.bookslyserver.domain.shop.entity.BusinessSchedule.BusinessSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.entity.Shop.ShopImage;
import kyonggi.bookslyserver.domain.shop.repository.BusinessScheduleRepository;
import kyonggi.bookslyserver.domain.shop.repository.ShopImageRepository;
import kyonggi.bookslyserver.domain.shop.repository.ShopRepository;
import kyonggi.bookslyserver.domain.user.entity.ShopOwner;
import kyonggi.bookslyserver.domain.user.repository.ShopOwnerRepository;
import kyonggi.bookslyserver.global.error.exception.BusinessException;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static kyonggi.bookslyserver.global.error.ErrorCode.BAD_REQUEST;
import static kyonggi.bookslyserver.global.error.ErrorCode.ENTITY_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;

    private final BusinessScheduleRepository businessScheduleRepository;

    private final ShopImageRepository shopImageRepository;

    private final ShopOwnerRepository shopOwnerRepository;
    @Transactional
    public ShopRegisterDto join(Long ownerId, ShopCreateRequestDto requestDto) {

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
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND));
        if (shop.getShopOwner().getId() != ownerId) {
            throw new BusinessException(BAD_REQUEST);
        }
        return shop;
    }

}

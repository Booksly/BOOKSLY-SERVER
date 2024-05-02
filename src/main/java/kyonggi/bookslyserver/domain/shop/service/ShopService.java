package kyonggi.bookslyserver.domain.shop.service;


import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.shop.dto.BusinessScheduleDto;
import kyonggi.bookslyserver.domain.shop.dto.request.ShopCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.response.menu.MenuReadDto;
import kyonggi.bookslyserver.domain.shop.dto.response.shop.*;
import kyonggi.bookslyserver.domain.shop.entity.BusinessSchedule.BusinessSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.entity.Shop.ShopImage;
import kyonggi.bookslyserver.domain.shop.repository.BusinessScheduleRepository;
import kyonggi.bookslyserver.domain.shop.repository.ShopImageRepository;
import kyonggi.bookslyserver.domain.shop.repository.ShopRepository;
import kyonggi.bookslyserver.domain.user.entity.ShopOwner;
import kyonggi.bookslyserver.domain.user.repository.ShopOwnerRepository;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;

    private final BusinessScheduleRepository businessScheduleRepository;

    private final ShopImageRepository shopImageRepository;

    private final ShopOwnerRepository shopOwnerRepository;


    public ShopUserReadOneDto findOne(Long id){
        Optional<Shop> shop = shopRepository.findById(id);
        if(!shop.isPresent()){
            throw new EntityNotFoundException();
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
    public void delete(Long id){
        Optional<Shop> shop = shopRepository.findById(id);
        ShopOwner owner = shop.get().getShopOwner();
        owner.deleteShop(shop.get());
        shopRepository.deleteById(id);
    }






}

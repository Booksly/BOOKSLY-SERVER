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

    @Transactional //ShopService 클래스에 이미 @Transcational을 걸었으므로 중복 작성입니다.
    public ShopRegisterDto join(Long ownerId, ShopCreateRequestDto requestDto) {

        if(shopRepository.existsByName(requestDto.getName())){
            //DB에 등록된 '모든가게'의 이름이 중복이 되지 않아야하는게 아니라, 해당 가게 주인이 가지고 있는 가게들과 이름이 중복되지 않아야합니다.
            throw new BusinessException(SHOP_NAME_ALREADY_EXIST);
        }
        Shop shop = Shop.createShop(requestDto);
        shopRepository.save(shop);
        List<BusinessSchedule> businessScheduleList = requestDto.getBusinessScheduleList();
        for(BusinessSchedule businessSchedule : businessScheduleList){
            shop.getBusinessSchedule(businessSchedule); //이름이 부적절합니다.메소드의 역할에 맞는 이름이 아닙니다. get-이 아닌 add-가 적절할 것 같아요.
        }

        List<ShopImage> shopImages = requestDto.getShopImageList();//이미지 s3 업로드 코드 추가, 이미지는 하나만
        for(ShopImage shopImage : shopImages){
            shop.getShopImage(shopImage); //이름이 부적절합니다. 메소드의 역할에 맞는 이름이 아닙니다.
        }

        Optional<ShopOwner> owner = shopOwnerRepository.findById(ownerId); //해당 기업회원이 가게의 주인이 맞는지 검증하는 코드를 추가해주세요. 또한 이러한 검증은 가게 엔티티 생성 후가 아닌 가게 엔티티 생성 전에 이루어지는 것이 좋습니다.
        shop.getShopOwner(owner);
        shop.setCreatedAt(LocalDate.now()); //해당 코드 작성하지 않아도 해당 필드의 값은 자동으로 채워집니다.
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

    public ShopOwnerMainReadOneDto readMain(Long id){
        Optional<Shop> shop = shopRepository.findById(id);
        if(!shop.isPresent()){
            throw new EntityNotFoundException(SHOP_NOT_FOUND);
        }
        return new ShopOwnerMainReadOneDto(shop.get());
    }

    public ShopOwnerDetailReadOneDto readOne(Long id){
        Optional<Shop> shop = shopRepository.findById(id);
        if(!shop.isPresent()){
            throw new EntityNotFoundException(SHOP_NOT_FOUND);
        }
        List<BusinessScheduleDto> businessScheduleDtos = new ArrayList<>();

        for(BusinessSchedule businessSchedule : shop.get().getBusinessSchedules()){
            businessScheduleDtos.add(new BusinessScheduleDto(businessSchedule));
        }


        return new ShopOwnerDetailReadOneDto(shop.get(), businessScheduleDtos);
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

package kyonggi.bookslyserver.domain.shop.dto.response.shop;

import kyonggi.bookslyserver.domain.review.entity.Review;
import kyonggi.bookslyserver.domain.shop.dto.response.employee.ShopOwnerMainEmployeesDto;
import kyonggi.bookslyserver.domain.shop.dto.response.menu.ShopOwnerMainMenusDto;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ShopOwnerMainReadOneDto {
    private Long id;
    private String name;
    private int reviewNum;
    private float reviewAvg;
    private String description;
    private AddressDto address;
    private String detailAddress;
    private String phoneNumber;
    private List<BusinessScheduleDto> businessSchedules;
    private String imgUrl;
    private List<ShopOwnerMainMenusDto> menus;
    private List<ShopOwnerMainEmployeesDto> employees;

    public ShopOwnerMainReadOneDto(Shop shop) {
        this.businessSchedules = new ArrayList<>();
        this.menus = new ArrayList<>();
        this.employees = new ArrayList<>();

        this.id = shop.getId();
        this.name = shop.getName();
        if (shop.getReviews() != null) {
            this.reviewNum = shop.getReviews().size();
            float avg = 0f;
            int reviewNum = 0;
            for (Review review : shop.getReviews()) {
                avg += review.getRating();
                reviewNum++;
            }
            this.reviewAvg = Float.parseFloat(String.format("%.1f", avg / reviewNum));
        }
        else{
            this.reviewNum = 0;
            this.reviewAvg = 0;
        }
        this.description = shop.getIntroduction();
        this.address = new AddressDto(shop.getAddress());
        this.detailAddress = shop.getDetailAddress();
        this.phoneNumber = shop.getPhoneNumber();
        if(shop.getShopImages() != null){
            this.imgUrl = shop.getShopImages().get(0).getImgUri();
        }

        this.businessSchedules = shop.getBusinessSchedules().stream().map(businessSchedule -> new BusinessScheduleDto(businessSchedule)).collect(Collectors.toList());
        this.menus = shop.getMenus().stream().map(menu -> new ShopOwnerMainMenusDto(menu)).collect(Collectors.toList());
        this.employees = shop.getEmployees().stream().map(employee -> new ShopOwnerMainEmployeesDto(employee)).collect(Collectors.toList());
    }
}

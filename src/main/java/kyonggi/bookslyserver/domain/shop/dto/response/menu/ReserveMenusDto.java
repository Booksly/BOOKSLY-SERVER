package kyonggi.bookslyserver.domain.shop.dto.response.menu;

import kyonggi.bookslyserver.domain.event.entity.closeEvent.ClosingEventMenu;
import kyonggi.bookslyserver.domain.event.entity.timeEvent.EmployeeTimeEvent;
import kyonggi.bookslyserver.domain.event.entity.timeEvent.TimeEventMenu;
import kyonggi.bookslyserver.domain.shop.entity.Employee.EmployeeMenu;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import lombok.Data;

@Data
public class ReserveMenusDto {
    private Long menuId;

    private Long employeeMenuId;

    private Boolean isEvent;

    private String eventDescribe;

    private int discountRate;

    private String menuCategoryName;

    private String menuName;

    private int price;

    public ReserveMenusDto(Menu menu, EmployeeMenu employeeMenu){
        String closingEventDescribe = "";
        String timeEventDescribe = "";

        if(menu.getClosingEventMenus() != null){
            for(ClosingEventMenu closingEventMenu : menu.getClosingEventMenus()){
                if(closingEventMenu.getClosingEvent().getEmployee() == employeeMenu.getEmployee()){
                    this.isEvent = true;
                    closingEventDescribe = closingEventMenu.getClosingEvent().getEventMessage();
                    this.discountRate = closingEventMenu.getClosingEvent().getDiscountRate();
                }

            }
        }

        if(menu.getTimeEventMenus() != null){
            for(TimeEventMenu timeEventMenu : menu.getTimeEventMenus()){
                for(EmployeeTimeEvent employeeTimeEvent : timeEventMenu.getTimeEvent().getEmployeeTimeEvents()){
                    if(employeeTimeEvent.getEmployee() == employeeMenu.getEmployee()){
                        this.isEvent = true;
                        timeEventDescribe = employeeTimeEvent.getTimeEvent().getTitle();
                        this.discountRate = employeeTimeEvent.getTimeEvent().getDiscountRate();
                    }
                }
            }
        }




        if(closingEventDescribe != ""){
            this.eventDescribe = "[ClosingEvent]" + closingEventDescribe;
        }
        else{
            this.eventDescribe = "[TimeEvent]" + timeEventDescribe;
        }

        if(closingEventDescribe.equals("") && timeEventDescribe.equals("")){
            this.eventDescribe = "이벤트 메뉴가 아닙니다!";
            this.discountRate = 0;
            this.isEvent = false;
        }


        this.menuId = menu.getId();
        this.employeeMenuId = employeeMenu.getId();
        this.menuCategoryName = menu.getMenuCategory().getName();
        this.menuName = menu.getMenuName();
        this.price = menu.getPrice();
    }
}

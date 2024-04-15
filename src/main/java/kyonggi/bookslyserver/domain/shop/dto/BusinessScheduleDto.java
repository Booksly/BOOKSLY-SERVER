package kyonggi.bookslyserver.domain.shop.dto;

import kyonggi.bookslyserver.domain.shop.entity.BusinessSchedule.BusinessSchedule;
import kyonggi.bookslyserver.domain.shop.entity.BusinessSchedule.DayName;
import lombok.Data;

@Data
public class BusinessScheduleDto {
    private DayName day;
    private String openAt;
    private String closeAt;
    private boolean isHoliday;

    public BusinessScheduleDto(BusinessSchedule b){
        day = b.getDay();
        openAt = b.getOpenAt();
        closeAt = b.getCloseAt();
        isHoliday = b.isHoliday();
    }

}

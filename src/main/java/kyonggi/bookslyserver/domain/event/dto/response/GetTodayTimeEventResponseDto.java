package kyonggi.bookslyserver.domain.event.dto.response;

import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record GetTodayTimeEventResponseDto(
        Long shopId,
        String shopName,
        String shopImg,
        float rating,
        LocalTime time,
        String message
) {
    public static GetTodayTimeEventResponseDto of(ReservationSchedule eventReservationSchedule) {
        Shop shop = eventReservationSchedule.getShop();

        return GetTodayTimeEventResponseDto.builder()
                .shopId(shop.getId())
                .shopName(shop.getName())
                .shopImg(shop.getShopImages().get(0).getImgUri())
                .rating(shop.getRatingByReview())
                .time(eventReservationSchedule.getStartTime())
                .message(eventReservationSchedule.getTimeEventSchedule().getTimeEvent().getTitle()).build();
    }
}

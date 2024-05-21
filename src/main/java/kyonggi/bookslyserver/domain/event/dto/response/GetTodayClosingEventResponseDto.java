package kyonggi.bookslyserver.domain.event.dto.response;

import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import kyonggi.bookslyserver.domain.review.entity.Review;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record GetTodayClosingEventResponseDto(
        Long shopId,
        String shopName,
        String shopImg,
        float rating,
        LocalTime time,
        String message
) {
    public static GetTodayClosingEventResponseDto of(ReservationSchedule eventReservationSchedule){

        Shop shop = eventReservationSchedule.getShop();

        return GetTodayClosingEventResponseDto.builder()
                .shopId(shop.getId())
                .shopName(shop.getName())
                .shopImg(shop.getShopImages().get(0).getImgUri())
                .rating(shop.getRatingByReview())
                .message(eventReservationSchedule.getClosingEvent().getEventMessage()).build();}
}

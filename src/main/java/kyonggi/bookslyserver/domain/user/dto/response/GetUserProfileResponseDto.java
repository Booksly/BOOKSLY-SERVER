package kyonggi.bookslyserver.domain.user.dto.response;

import kyonggi.bookslyserver.domain.user.entity.User;
import lombok.Builder;

import java.util.stream.Collectors;

@Builder
public record GetUserProfileResponseDto(
        String nickname,
        String profileImgUri,
        String email,
        int reviewNum,
        int favoriteShopNum,
        int visitNum

)  {
    public static GetUserProfileResponseDto of(User user) {
        return GetUserProfileResponseDto.builder()
                .nickname(user.getNickname())
                .profileImgUri(user.getProfileImgUrl())
                .email(user.getEmail())
                .reviewNum(user.getReviews().size())
                .favoriteShopNum(user.getFavoriteShops().size())
                .visitNum(user.getReservations().stream().filter(reservation -> reservation.isConfirmed()).collect(Collectors.toList()).size())
                .build();
    }
}

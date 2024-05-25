package kyonggi.bookslyserver.domain.user.dto.response;

import kyonggi.bookslyserver.domain.user.entity.User;
import lombok.Builder;

@Builder
public record GetUserDetailInfoResponseDto(
        String phoneNumber,
        String nickname
) {
    public static GetUserDetailInfoResponseDto of(User user) {
        return GetUserDetailInfoResponseDto.builder()
                .phoneNumber(user.getPhoneNum())
                .nickname(user.getNickname()).build();
    }
}

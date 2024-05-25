package kyonggi.bookslyserver.domain.user.dto.response;

import kyonggi.bookslyserver.domain.user.entity.User;
import lombok.Builder;

@Builder
public record UpdateUserInfoResponseDto(
        String phoneNumber,
        String nickname
) {
    public static UpdateUserInfoResponseDto of(User user) {
        return UpdateUserInfoResponseDto.builder()
                .phoneNumber(user.getPhoneNum())
                .nickname(user.getNickname()).build();
    }
}

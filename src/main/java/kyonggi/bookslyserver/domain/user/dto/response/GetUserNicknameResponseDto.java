package kyonggi.bookslyserver.domain.user.dto.response;

import kyonggi.bookslyserver.domain.user.entity.User;
import lombok.Builder;

@Builder
public record GetUserNicknameResponseDto(
        String nickname
) {
    public static GetUserNicknameResponseDto of(User user) {
        return GetUserNicknameResponseDto.builder()
                .nickname(user.getNickname())
                .build();
    }
}

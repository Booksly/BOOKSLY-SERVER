package kyonggi.bookslyserver.domain.notice.dto;

import lombok.Data;

// 반환 elements를 매핑하기 위한 dto
@Data
public class KaKaoFriendsListDTO {
    private String id;
    private String uuid;
    private boolean favorite;
    private String profile_nickname;
    private String profile_thumbnail_image;
}

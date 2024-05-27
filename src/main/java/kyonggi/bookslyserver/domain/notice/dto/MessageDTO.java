package kyonggi.bookslyserver.domain.notice.dto;

import lombok.Data;

@Data
public class MessageDTO {
    private String objType;
    private String text;
    private String webUrl;
    private String mobileUrl;
    private String btnTitle;
}
// 카카오 API에 Request할 때 필수로 필요한 값들
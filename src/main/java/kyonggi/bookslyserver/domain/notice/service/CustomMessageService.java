package kyonggi.bookslyserver.domain.notice.service;

import kyonggi.bookslyserver.domain.notice.dto.KaKaoFriendsListDTO;
import kyonggi.bookslyserver.domain.notice.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// 실제로 보낼 메세지를 작성한 클래스
@Service
public class CustomMessageService {
    @Autowired
    MessageService messageService;

    public boolean sendMyMessage() {
        MessageDTO myMsg = new MessageDTO();
        myMsg.setBtnTitle("자세히보기");
        myMsg.setMobileUrl("");
        myMsg.setObjType("text");
        myMsg.setWebUrl("");
        myMsg.setText("메시지 테스트입니다.");

        String accessToken = AuthService.authToken;

        return messageService.sendMessage(accessToken, myMsg);
    }

    public boolean sendMessageToFriend(){
        return true;
    }
    public List<KaKaoFriendsListDTO> getFriendsList(){
        return messageService.getFriendsList(AuthService.authToken);
    }
}

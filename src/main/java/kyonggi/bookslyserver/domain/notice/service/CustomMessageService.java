package kyonggi.bookslyserver.domain.notice.service;

import kyonggi.bookslyserver.domain.notice.dto.KaKaoFriendsListDTO;
import kyonggi.bookslyserver.domain.notice.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

// 실제로 보낼 메세지를 작성한 클래스
@Service
public class CustomMessageService {
    @Autowired
    MessageService messageService;

    public boolean sendMyMessage() {
        MessageDTO myMsg = new MessageDTO();
        myMsg.setBtnTitle("자세히 보기");
        myMsg.setMobileUrl("");
        myMsg.setObjType("text");
        myMsg.setWebUrl("");
        myMsg.setText("테스트 메세지입니다.");

        String accessToken = AuthService.authToken;

        return messageService.sendMessage(accessToken, myMsg);
    }

    public ResponseEntity<?> sendMessageToFriend(List<KaKaoFriendsListDTO> friends){
        MessageDTO myMsg=new MessageDTO();
        myMsg.setBtnTitle("자세히 보기");
        myMsg.setMobileUrl("");
        myMsg.setObjType("text");
        myMsg.setWebUrl("");
        myMsg.setText("너 예약된거야");

        String [] uuidArray= friends.stream()
                .map(KaKaoFriendsListDTO::getUuid)
                .toArray(String[]::new);
        String accessToken = AuthService.authToken;

        return messageService.sendMessageToFriend(accessToken,myMsg,uuidArray);
    }
    public List<KaKaoFriendsListDTO> getFriendsList(){
        return messageService.getFriendsList(AuthService.authToken);
    }
}

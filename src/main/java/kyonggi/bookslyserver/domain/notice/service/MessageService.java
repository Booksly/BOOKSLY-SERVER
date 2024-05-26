package kyonggi.bookslyserver.domain.notice.service;

import kyonggi.bookslyserver.domain.notice.dto.KaKaoFriendsListDTO;
import kyonggi.bookslyserver.domain.notice.dto.MessageDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * HttpCallService를 상속받아 실제 Message 전송을 담당할 클래스
 */
@Service
public class MessageService extends HttpCallService{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String MSG_SEND_SERVICE_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
    private static final String MSG_SEND_TO_FRIEND_SERVICE_URL="https://kapi.kakao.com/v1/api/talk/friends/message/default/send";
    private static final String FRIENDS_LIST_SERVICE_URL="https://kapi.kakao.com/v1/api/talk/friends";
    private static final String SEND_SUCCESS_MSG = "메시지 전송에 성공했습니다.";
    private static final String SEND_FAIL_MSG = "메시지 전송에 실패했습니다.";
    private static final String SUCCESS_CODE = "0";
    //kakao api에서 메세지 전송 후 반환 해주는 success code 값

    public boolean sendMessage(String accessToken, MessageDTO msgDto) {
        JSONObject linkObj = new JSONObject();
        linkObj.put("web_url", msgDto.getWebUrl());
        linkObj.put("mobile_web_url", msgDto.getMobileUrl());

        JSONObject templateObj = new JSONObject();
        templateObj.put("object_type", msgDto.getObjType());
        templateObj.put("text", msgDto.getText());
        templateObj.put("link", linkObj);
        templateObj.put("button_title", msgDto.getBtnTitle());


        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type", APP_TYPE_URL_ENCODED);
        header.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("template_object", templateObj.toString());

        HttpEntity<?> messageRequestEntity = httpClientEntity(header, parameters);

        String resultCode = "";
        ResponseEntity<String> response = httpRequest(MSG_SEND_SERVICE_URL, HttpMethod.POST, messageRequestEntity);
        JSONObject jsonData = new JSONObject(response.getBody());
        resultCode = jsonData.get("result_code").toString();

        return successCheck(resultCode);
    }
    public ResponseEntity<?> sendMessageToFriend(String accessToken, MessageDTO msgDto, String [] uuidArray){
        JSONObject linkObj=new JSONObject();
        linkObj.put("web_url", msgDto.getWebUrl());
        linkObj.put("mobile_web_url", msgDto.getMobileUrl());

        JSONObject templateObj = new JSONObject();
        templateObj.put("object_type", msgDto.getObjType());
        templateObj.put("text", msgDto.getText());
        templateObj.put("link", linkObj);
        templateObj.put("button_title", msgDto.getBtnTitle());

        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type", APP_TYPE_URL_ENCODED);
        header.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        // String [] -> JSON 배열 형식
        String uuidArrayString = "[" + Arrays.stream(uuidArray).map(uuid -> "\"" + uuid + "\"").collect(Collectors.joining(",")) + "]";

        parameters.add("receiver_uuids",uuidArrayString);
        parameters.add("template_object", templateObj.toString());
        HttpEntity<?> requestEntity=httpClientEntity(header,parameters);

        return httpRequest(MSG_SEND_TO_FRIEND_SERVICE_URL,HttpMethod.POST,requestEntity);
    }
    public boolean successCheck(String resultCode) {
        if(resultCode.equals(SUCCESS_CODE)) {
            logger.info(SEND_SUCCESS_MSG);
            return true;
        }else {
            logger.debug(SEND_FAIL_MSG);
            return false;
        }

    }

    /**
     * 친구 목록 가져오기
     */
    public List<KaKaoFriendsListDTO> getFriendsList(String accessToken) {
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(header);

        ResponseEntity<String> response = httpRequest(FRIENDS_LIST_SERVICE_URL, HttpMethod.GET, entity);

        List<KaKaoFriendsListDTO> list = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(response.getBody());
//        Integer total= jsonObject.getInt("total_count");
//        System.out.println(total);
        JSONArray elements = (JSONArray) jsonObject.get("elements");

        for (Object element : elements) {
            KaKaoFriendsListDTO dto = new KaKaoFriendsListDTO();
            JSONObject obj = (JSONObject) element;
            dto.setId(obj.get("id").toString());
            dto.setUuid(obj.get("uuid").toString());
            dto.setFavorite((boolean) obj.get("favorite"));
            dto.setProfile_nickname(obj.get("profile_nickname").toString());
            dto.setProfile_thumbnail_image(obj.get("profile_thumbnail_image").toString());

            list.add(dto);
        }

        return list;
    }
}

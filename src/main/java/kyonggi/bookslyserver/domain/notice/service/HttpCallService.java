package kyonggi.bookslyserver.domain.notice.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class HttpCallService {
    protected static final String APP_TYPE_URL_ENCODED = "application/x-www-form-urlencoded;charset=UTF-8";
    protected static final String APP_TYPE_JSON = "application/json;charset=UTF-8";
    /**
     * HTTP 요청에 사용할 HTTP 객체 생성
     * 객체는 HTTP 헤더와 바디를 포함
     * @param header 헤더 정보를 포함
     * @param params 바디 정보를 포함
     * @return 
     */
    public HttpEntity<?> httpClientEntity(HttpHeaders header,Object params){
        HttpHeaders requestHeaders=header;
        if (params==null||"".equals(params)) return new HttpEntity<>(requestHeaders);
        else return new HttpEntity<>(params,requestHeaders);
    }

    /**
     * HTTP 요청 전송
     * @param url 요청할 url 문자열
     * @param method HTTP 메소드 ex) GET, POST
     * @param entity 요청에 사용할 HttpEntity 객체
     * @return
     */
    public ResponseEntity<String> httpRequest(String url, HttpMethod method, HttpEntity<?> entity){
        RestTemplate restTemplate=new RestTemplate();
        return restTemplate.exchange(url,method,entity,String.class);
    }

}

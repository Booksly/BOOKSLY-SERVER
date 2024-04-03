package kyonggi.bookslyserver.domain.user.util;

import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoApiKeyException;
import net.nurigo.sdk.message.exception.NurigoBadRequestException;
import net.nurigo.sdk.message.exception.NurigoException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AuthUtil {

    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecretKey;
    @Value("${coolsms.sendFrom.phoneNum}")
    private String sendFromNum;

    private DefaultMessageService messageService;

    @PostConstruct
    private void init(){
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
    }

    public SingleMessageSentResponse sendOneSMS(String to, int authenticationCode) throws NurigoBadRequestException {
        Message message = new Message();

        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom(sendFromNum);
        message.setTo(to);
        message.setText("[booksly] 아래의 인증번호를 입력해주세요\n" + authenticationCode);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        return response;
    }

    public int createAuthCode() {
        Random generator = new Random();
        generator.setSeed(System.currentTimeMillis());
        return generator.nextInt(1000000) % 1000000;
    }

}

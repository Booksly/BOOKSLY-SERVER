package kyonggi.bookslyserver.domain.user.service;

import jakarta.validation.Valid;
import kyonggi.bookslyserver.domain.user.dto.request.OwnerVerifyRequestDto;
import kyonggi.bookslyserver.domain.user.dto.request.SendSMSRequestDto;
import kyonggi.bookslyserver.domain.user.dto.response.OwnerVerifyResponseDto;
import kyonggi.bookslyserver.domain.user.dto.response.SendSMSResponseDto;
import kyonggi.bookslyserver.domain.user.repository.UserRepository;
import kyonggi.bookslyserver.global.util.AuthUtil;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.CustomNurigoException;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import kyonggi.bookslyserver.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.exception.NurigoBadRequestException;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserRepository userRepository;
    private final AuthUtil authUtil;
    private final RedisUtil redisUtil;

    public SendSMSResponseDto sendMessage(SendSMSRequestDto sendSMSRequestDto) {

        String receivingNumber = sendSMSRequestDto.receivingNumber();
        int authCode = authUtil.createAuthCode();

        SingleMessageSentResponse messageSentResponse = null;

        try {
            messageSentResponse = authUtil.sendOneSMS(receivingNumber, authCode);
        } catch (NurigoBadRequestException e) {
            throw new CustomNurigoException();
        }

        //인증 시간 5분 설정
        redisUtil.setDataExpire(String.valueOf(authCode), receivingNumber,  60 * 5L);

        return SendSMSResponseDto.builder()
                .statusCode(messageSentResponse.getStatusCode())
                .sendTo(messageSentResponse.getTo())
                .sendFrom(messageSentResponse.getFrom())
                .statusMessage(messageSentResponse.getStatusMessage()).build();
    }

    public SendSMSResponseDto sendMessageToUser(Long userId, SendSMSRequestDto sendSMSRequestDto) {

        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        return sendMessage(sendSMSRequestDto);
    }

    public OwnerVerifyResponseDto ownerVerifyByCode(OwnerVerifyRequestDto ownerVerifyRequestDto) {
        String code = ownerVerifyRequestDto.code();
        return null;
    }
}

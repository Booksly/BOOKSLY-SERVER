package kyonggi.bookslyserver.domain.user.service;

import kyonggi.bookslyserver.domain.user.constant.Role;
import kyonggi.bookslyserver.domain.user.entity.User;
import kyonggi.bookslyserver.domain.user.repository.UserRepository;
import kyonggi.bookslyserver.global.auth.principal.userInfo.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    /**
     * 유저정보를 받아 회원가입한다.
     * 이미 가입되어 있다면 닉네임과 프로필이미지를 업데이트한다.
     *
     * @param userInfo 로그인을 요청하는 유저의 정보
     * @return 유저 객체
     */
    public User getOrCreateUser(OAuth2UserInfo userInfo) {
        String loginId = userInfo.getIdByProvider();
        String email = userInfo.getEmail();
        String nickname = userInfo.getNickname();
        String profileImgUrl = userInfo.getProfileImgUrl();

        return userRepository.findByLoginId(loginId)
                .map(user -> {
                    user.updateUserInfo(nickname, profileImgUrl);
                    return user;
                })
                .orElseGet(() -> createUser(email,loginId,nickname,profileImgUrl));
    }

    private User createUser(String email, String loginId, String nickname, String profileImgUrl) {
        User createdUser = User.builder()
                .email(email)
                .loginId(loginId)
                .nickname(nickname)
                .profileImgUrl(profileImgUrl)
                .role(Role.ROLE_USER)
                .isVerified(false)
                .build();


        return userRepository.save(createdUser);
    }

}

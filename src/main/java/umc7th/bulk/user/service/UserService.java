package umc7th.bulk.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import umc7th.bulk.global.error.GeneralErrorCode;
import umc7th.bulk.global.error.exception.CustomException;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final WebClient webClient;
    private final OAuth2AuthorizedClientService authorizedClientService;

    private static final String UNLINK_URL = "https://kapi.kakao.com/v1/user/unlink";

    @Transactional
    public User saveUser(String kakaoId, String email, String gender, String birthyear, String accessToken, String refreshToken) {

        if (userRepository.existsByKakaoId(kakaoId)) {
            throw new CustomException(GeneralErrorCode.DUPLICATE_USER);
        }

        if (userRepository.existsByEmail(email)) {
            throw new CustomException(GeneralErrorCode.DUPLICATE_USER);
        }

        User user = User.builder()
                .kakaoId(kakaoId)
                .email(email)
                .gender(gender)
                .birthyear(birthyear)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .recordComplete(false)//로그인이 먼저 되면 이부분이 우선적으로 저장되는데, recordComplete도 false로 같이 저장하는게 좋을것같아요,,
                .build();

        return userRepository.save(user);
    }

    public void unlinkUser(OAuth2User oAuth2User) {
        String clientRegistrationId = "kakao"; // OAuth2 Client 등록 ID
        String principalName = oAuth2User.getName();

        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(clientRegistrationId, principalName);
        if (authorizedClient == null || authorizedClient.getAccessToken() == null) {
            throw new CustomException(GeneralErrorCode.UNAUTHORIZED_401);
        }

        String accessToken = authorizedClient.getAccessToken().getTokenValue();

        webClient.post()
                .uri(UNLINK_URL)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .onStatus(
                        status -> status.isError(), // 상태 코드가 에러인지 확인
                        response -> Mono.error(new CustomException(GeneralErrorCode.BAD_REQUEST_400))
                )
                .toBodilessEntity()
                .block();
    }

    public boolean userExists(String kakaoId) {
        return userRepository.existsByKakaoId(kakaoId);
    }


}

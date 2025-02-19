package umc7th.bulk.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import umc7th.bulk.character.entity.BulkCharacter;
import umc7th.bulk.character.repository.BulkCharacterRepository;
import umc7th.bulk.global.error.GeneralErrorCode;
import umc7th.bulk.global.error.exception.CustomException;
import umc7th.bulk.group.entity.Group;
import umc7th.bulk.group.repository.GroupRepository;
import umc7th.bulk.user.annotation.CurrentUser;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.exception.UserErrorCode;
import umc7th.bulk.user.exception.UserException;
import umc7th.bulk.user.principal.PrincipalDetails;
import umc7th.bulk.user.repository.UserRepository;

import java.time.LocalDateTime;

import static umc7th.bulk.user.dto.UserDTO.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final WebClient webClient;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final BulkCharacterRepository bulkCharacterRepository;
    private final GroupRepository groupRepository;

    private static final String UNLINK_URL = "https://kapi.kakao.com/v1/user/unlink";

    @Transactional
    public User saveUser(String kakaoId, String email, String gender, String birthyear, String accessToken, String refreshToken) {

        if (userRepository.existsByKakaoId(kakaoId)) {
            throw new CustomException(GeneralErrorCode.DUPLICATE_USER);
        }

        if (userRepository.existsByEmail(email)) {
            throw new CustomException(GeneralErrorCode.DUPLICATE_USER);
        }

        // 가입시 캐릭터 같이 생성
        BulkCharacter bulkCharacter = BulkCharacter.builder()
                .name(email + "_BULK")
                .level(0)
                .point(0)
                .build();
        bulkCharacterRepository.save(bulkCharacter);

        // 기존 그룹 중 10명 미만인 그룹 찾기 (없으면 새 그룹 생성)
        Group group = groupRepository.findGroupWithSpace().orElseGet(() -> {
            Group newGroup = Group.builder()
                    .groupName("Group_" + System.currentTimeMillis()) // 유니크한 그룹 이름 생성
                    .currentStage(1)
                    .endDate(LocalDateTime.now().plusDays(7)) // 그룹 종료일 7일 후 설정
                    .build();
            return groupRepository.save(newGroup);
        });

        User user = User.builder()
                .kakaoId(kakaoId)
                .email(email)
                .gender(gender)
                .birthyear(birthyear)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .recordComplete(false)//로그인이 먼저 되면 이부분이 우선적으로 저장되는데, recordComplete도 false로 같이 저장하는게 좋을것같아요,,
                .bulkCharacter(bulkCharacter)
                .curCalories(0L)
                .curCarbos(0L)
                .curProteins(0L)
                .curFats(0L)
                .group(group)
                .build();

        group.addMember(user);

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

    public CharacterDTO getCharacter(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.USER_NOT_FOUND)
        );
        return new CharacterDTO(user);
    }

    public UserNutritionDTO getUserNutrition(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.USER_NOT_FOUND)
        );
        return new UserNutritionDTO(user);
    }

    public void updateTokens(String kakaoId, String accessToken, String refreshToken) {
        User user = userRepository.findByKakaoId(kakaoId).orElseThrow(() ->
                new UserException(UserErrorCode.USER_NOT_FOUND));

        user.updateTokens(accessToken, refreshToken);
        userRepository.save(user); // 토큰 정보 업데이트 후 저장
    }

    public User getUserByKakaoId(String kakaoId) {
        return userRepository.findByKakaoId(kakaoId).orElseThrow(() ->
                new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    public User findByKakaoId(String kakaoId) {
        return userRepository.findByKakaoId(kakaoId).orElseThrow(()->
                new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    /*
    public User getAuthenticatedUserInfo() {
        String kakaoId;
        try {
            kakaoId = SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception e) {
            throw new UserException(UserErrorCode.USER_NOT_FOUND);
        }

        User user = this.findByKakaoId(kakaoId);

        if (user == null) {
            throw new UserException(UserErrorCode.USER_NOT_FOUND);
        }
        return user;

    }*/
    public User getAuthenticatedUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserException(UserErrorCode.USER_NOT_AUTHENTICATED);
        }

        Object principal = authentication.getPrincipal();
        System.out.println("🔍 Principal Type: " + principal.getClass().getName());
        System.out.println("🔍 Principal Value: " + principal);

        String email;
        if (principal instanceof String) {
            email = (String) principal;
        } else if (principal instanceof PrincipalDetails) {
            email = ((PrincipalDetails) principal).getUsername();
        } else {
            throw new UserException(UserErrorCode.USER_NOT_AUTHENTICATED);
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }



}

package umc7th.bulk.user.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import umc7th.bulk.user.service.UserService;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 사용자 정보 추출
        String kakaoId = String.valueOf(attributes.get("id"));
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String email = (String) kakaoAccount.get("email");
        String gender = (String) kakaoAccount.get("gender");
        String birthyear = (String) kakaoAccount.get("birthyear");

        // OAuth2AuthorizedClient에서 토큰 추출
        Optional<OAuth2AuthorizedClient> optionalClient = Optional.ofNullable(
                authorizedClientService.loadAuthorizedClient("kakao", oAuth2User.getName())
        );

        String accessToken = optionalClient.map(OAuth2AuthorizedClient::getAccessToken)
                .map(token -> token.getTokenValue())
                .orElse(null);

        String refreshToken = optionalClient.map(OAuth2AuthorizedClient::getRefreshToken)
                .map(token -> token.getTokenValue())
                .orElse(null);

        boolean isExistingUser = userService.userExists(kakaoId);

        if (!isExistingUser) {
            // 새로운 사용자라면 사용자 저장
            userService.saveUser(kakaoId, email, gender, birthyear, accessToken, refreshToken);

            response.sendRedirect("https://bulkapp.site/report");
        } else {
            // 기존 사용자라면 토큰 정보 업데이트
            userService.updateTokens(kakaoId, accessToken, refreshToken);

            response.sendRedirect("/home/info");
        }

        // SecurityContext에 인증 정보 설정 (기존,신규 사용자 둘 다)
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                oAuth2User,null, oAuth2User.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        // System.out.println("Authentication: " + SecurityContextHolder.getContext().getAuthentication());
    }
}
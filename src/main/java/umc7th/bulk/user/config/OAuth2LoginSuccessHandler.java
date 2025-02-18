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
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2LoginSuccessHandler.class);

    private final UserService userService;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환을 위한 ObjectMapper

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // ✅ 사용자 정보 추출
        String kakaoId = String.valueOf(attributes.get("id"));
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String email = (String) kakaoAccount.get("email");
        String gender = (String) kakaoAccount.get("gender");
        String birthyear = (String) kakaoAccount.get("birthyear");

        // ✅ OAuth2AuthorizedClient에서 토큰 추출
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
        User user;
        String redirectUrl;

        if (!isExistingUser) {
            // ✅ 새로운 사용자 저장
            user = userService.saveUser(kakaoId, email, gender, birthyear, accessToken, refreshToken);
            logger.info("🆕 새로운 사용자 저장됨: {}", email);
            request.getSession().setAttribute("isFirstLogin", true); // ✅ 첫 로그인 플래그 설정
            redirectUrl = "https://bulkapp.site/report";
        } else {
            // ✅ 기존 사용자 토큰 업데이트
            userService.updateTokens(kakaoId, accessToken, refreshToken);
            user = userService.getUserByKakaoId(kakaoId);
            logger.info("🔄 기존 사용자 토큰 업데이트: {}", email);
            request.getSession().setAttribute("isFirstLogin", false);
            redirectUrl = "https://bulkapp.site/home";
        }

        // ✅ SecurityContext에 인증 정보 설정
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                oAuth2User, null, oAuth2User.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        if (request.getRequestURI().equals(redirectUrl)) {
            logger.warn("⚠️ 이미 {} 페이지에 있음. 리디렉션하지 않음.", redirectUrl);
            response.getWriter().write("Already logged in");
            return;
        }

        // ✅ 최종 리디렉트 실행
        logger.info("✅ 로그인 성공, 리디렉트할 페이지: {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }
}

package umc7th.bulk.user.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import umc7th.bulk.user.config.KakaoOAuth2Config;
import umc7th.bulk.user.dto.KakaoTokenResponse;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final KakaoOAuth2Config kakaoOAuth2Config;
    private final RestTemplate restTemplate;


    public KakaoTokenResponse requestKakaoToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        String clientId = kakaoOAuth2Config.getClientId();
        String clientSecert = kakaoOAuth2Config.getClientSecret();
        String redirectUri = kakaoOAuth2Config.getRedirectUri();
        System.out.println("clientId = " + clientId);
        System.out.println("clientSecert = " + clientSecert);
        System.out.println("redirectUri = " + redirectUri);

        String requestUrl = UriComponentsBuilder.fromHttpUrl(tokenUrl)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", kakaoOAuth2Config.getClientId())
                .queryParam("client_secret", kakaoOAuth2Config.getClientSecret())
                .queryParam("redirect_uri", kakaoOAuth2Config.getRedirectUri())
                .queryParam("code", code)
                .toUriString();

        System.out.println("✅ 최종 요청 URL: " + requestUrl);

        ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, null, String.class);

        System.out.println("✅ 카카오 응답: " + response.getBody());

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(response.getBody(), KakaoTokenResponse.class);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("❌ JSON 파싱 중 오류 발생: " + e.getMessage());
            }
        } else {
            throw new RuntimeException("카카오 토큰 요청 실패: " + response.getBody());
        }

    }
//
//    public String getKakaoUserId(String accessToken) {
//        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + accessToken);
//        headers.set("Content-Type", "application/x-www-form-urlencoded");
//
//        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//        ResponseEntity<String> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, requestEntity, String.class);
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            try {
//                ObjectMapper objectMapper = new ObjectMapper();
//                JsonNode root = objectMapper.readTree(response.getBody());
//                return root.get("id").asText();
//            } catch (Exception e) {
//                throw new RuntimeException("❌ 카카오 사용자 ID 파싱 실패: " + e.getMessage());
//            }
//        } else {
//            throw new RuntimeException("❌ 카카오 사용자 정보 요청 실패: " + response.getBody());
//        }
//    }
}

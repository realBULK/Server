package umc7th.bulk.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import umc7th.bulk.user.dto.KakaoTokenResponse;
import umc7th.bulk.user.service.KakaoAuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final KakaoAuthService kakaoAuthService;
    @PostMapping("/kakao/token")
    public ResponseEntity<?> getKakaoToken(@RequestParam("code") String code) {
        System.out.println("api ok");
        System.out.println("code = " + code);

        KakaoTokenResponse tokenResponse = kakaoAuthService.requestKakaoToken(code);
//
//        // 카카오 사용자 정보 가져오기 (추가 구현 필요)
//        String kakaoId = kakaoAuthService.getKakaoUserId(tokenResponse.getAccess_token());

        return ResponseEntity.ok(tokenResponse);
    }
}

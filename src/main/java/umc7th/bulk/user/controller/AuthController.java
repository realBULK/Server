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

        return ResponseEntity.ok(tokenResponse);
    }
}

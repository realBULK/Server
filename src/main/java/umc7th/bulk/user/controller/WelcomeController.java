package umc7th.bulk.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 로그인 후 보여줄 임시 페이지.보여줄 페이지 개발되면 삭제해도 되는 컨트롤러.
 * OAuth2LoginSuccessHandler 에 있는 "/welcome" 경로도 변경하면 됩니다.
 */

@RestController
public class WelcomeController {

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the application! You are successfully logged in.";
    }
}

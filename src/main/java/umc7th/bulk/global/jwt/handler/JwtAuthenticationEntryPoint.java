package umc7th.bulk.global.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import umc7th.bulk.global.apiPayload.CustomResponse;
import umc7th.bulk.global.error.GeneralErrorCode;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json; charset=UTF-8"); // 응답을 JSON 형식으로 설정
        response.setStatus(401); // HTTP 상태 코드를 401 (Unauthorized)로 설정

        CustomResponse<?> errorResponse = CustomResponse.fail(GeneralErrorCode.UNAUTHORIZED_401);

        ObjectMapper mapper = new ObjectMapper(); // ObjectMapper를 사용해 객체를 JSON으로 변환
        mapper.writeValue(response.getOutputStream(), errorResponse); // 에러 응답을 JSON 형태로 출력 스트림에 작성

    }
}

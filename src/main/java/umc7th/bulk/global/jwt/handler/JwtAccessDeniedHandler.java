package umc7th.bulk.global.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import umc7th.bulk.global.apiPayload.CustomResponse;
import umc7th.bulk.global.error.GeneralErrorCode;

import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 응답 타입을 JSON으로 설정
        response.setContentType("application/json; charset=UTF-8");
        // HTTP 상태 코드를 403 (Forbidden)으로 설정
        response.setStatus(403);

        // CustomResponse 객체를 생성하여 에러 응답 데이터 구성
        CustomResponse<?> errorResponse = CustomResponse.fail(GeneralErrorCode.FORBIDDEN_403);

        ObjectMapper mapper = new ObjectMapper(); // ObjectMapper를 이용해 객체를 JSON으로 변환
        mapper.writeValue(response.getOutputStream(), errorResponse); // 에러 응답을 JSON 형태로 출력 스트림에 작성
    }
}

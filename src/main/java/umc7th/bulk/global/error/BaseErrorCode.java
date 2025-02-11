package umc7th.bulk.global.error;

import org.springframework.http.HttpStatus;

// Exception 터질 때 에러에 대한 상세 내용과 함께
public interface BaseErrorCode {


    HttpStatus getStatus();

    String getCode();

    String getMessage();
}

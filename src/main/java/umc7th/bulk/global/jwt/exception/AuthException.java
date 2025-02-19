package umc7th.bulk.global.jwt.exception;

import umc7th.bulk.global.error.exception.CustomException;

public class AuthException extends CustomException {
    // JwtErrorCode를 받아 상위 클래스(GeneralException) 생성자를 호출하여 예외 초기화
    public AuthException(JwtErrorCode code) {
        super(code); // 부모 클래스의 생성자를 호출하면서 예외 코드 설정
    }

}

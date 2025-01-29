package umc7th.bulk.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GeneralErrorCode implements BaseErrorCode {

    // 400
    BAD_REQUEST_400(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),

    // 401
    UNAUTHORIZED_401(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),

    //403
    FORBIDDEN_403(HttpStatus.FORBIDDEN, "COMMON403", "접근이 금지되었습니다."),

    //404
    NOT_FOUND_404(HttpStatus.NOT_FOUND, "COMMON404", "요청한 자원을 찾을 수 없습니다."),
    GROUP_NOT_FOUND_404(HttpStatus.NOT_FOUND, "GROUP404", "존재하지 않는 그룹입니다."),


    //409
    DUPLICATE_USER(HttpStatus.CONFLICT, "USER409", "이미 가입된 사용자입니다."),

    //500
    INTERNAL_SERVER_ERROR_500(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 내부 오류가 발생했습니다."),
    ;


    private final HttpStatus status;
    private final String code;
    private final String message;
}

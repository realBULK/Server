package umc7th.bulk.global.apiPayload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import umc7th.bulk.global.error.BaseErrorCode;
import umc7th.bulk.global.error.exception.CustomException;
import umc7th.bulk.global.success.BaseSuccessCode;

@Builder
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) // 필드 값이 null인 경우 JSON 응답에 포함하지 않음
public class CustomResponse<T> {

    @JsonProperty("isSuccess")
    private boolean isSuccess;
    @JsonProperty("status")
    private HttpStatus httpStatus;
    @JsonProperty("code")
    private String code;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private T data;

    /**
     * 성공 반환: 내려줄 데이터가 없을 때
     * @return CustomResponse
     * @Param no param
     */
    public static CustomResponse<?> onSuccess(BaseSuccessCode baseSuccessCode) {
        return CustomResponse.builder()
                .isSuccess(true)
                .httpStatus(baseSuccessCode.getStatus())
                .code(String.valueOf(baseSuccessCode.getCode()))
                .message(baseSuccessCode.getMessage())
                .build();
    }


    /**
     * 성공, 내려줄 데이터가 있을 때.
     * @param data
     * @param <T>
     * @return CustomResponse<T>
     */
    public static <T> CustomResponse<T> onSuccess(BaseSuccessCode baseSuccessCode, T data) {
        return CustomResponse.<T>builder()
                .isSuccess(true)
                .httpStatus(baseSuccessCode.getStatus())
                .code(String.valueOf(baseSuccessCode.getCode()))
                .message(baseSuccessCode.getMessage())
                .data(data)
                .build();

    }


    /**
     * 실패
     * @param errorCode
     * @return CustomResponse
     */
    @ExceptionHandler(CustomException.class)
    public static CustomResponse<?> fail(BaseErrorCode errorCode) {
        return CustomResponse.builder()
                .isSuccess(false)
                .httpStatus(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }
}

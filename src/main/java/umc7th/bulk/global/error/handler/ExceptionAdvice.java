package umc7th.bulk.global.error.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import umc7th.bulk.global.apiPayload.CustomResponse;
import umc7th.bulk.global.error.BaseErrorCode;
import umc7th.bulk.global.error.GeneralErrorCode;
import umc7th.bulk.global.error.exception.CustomException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handle(CustomException e) {
        BaseErrorCode code = e.getCode();
        HttpStatus status = e.getCode().getStatus();
        return new ResponseEntity<>(CustomResponse.fail(code), status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handle(Exception e) {
        log.error("Exception: {}", e.getMessage());
        BaseErrorCode code = GeneralErrorCode.INTERNAL_SERVER_ERROR_500;
        HttpStatus status = code.getStatus();
        return new ResponseEntity<>(CustomResponse.fail(code), status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handle(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}

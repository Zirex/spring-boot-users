package cl.nisum.web.advice;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cl.nisum.exception.ApiException;
import cl.nisum.web.util.ResponseMessage;

@RestControllerAdvice
public class ApiAdvise {

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<ResponseMessage<Void>> getException(ApiException exception) {

        ResponseMessage<Void> msg = ResponseMessage.<Void>builder()
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(msg, exception.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseMessage<Void>> getValidateException(MethodArgumentNotValidException exception) {
        String errors = exception.getBindingResult().getAllErrors().stream().map(error -> error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        ResponseMessage<Void> msg = ResponseMessage.<Void>builder()
                .message(errors)
                .build();

        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
    }

}

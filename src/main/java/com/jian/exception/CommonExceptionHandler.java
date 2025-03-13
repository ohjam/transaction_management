package com.jian.exception;

import com.jian.common.http.response.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(BusinessException.class)
    public JsonResult<Object> handleBusinessException(BusinessException ex) {
        log.error(ex.getMsg(), ex);
        return new JsonResult<>().setSuccess(false).setCode(ex.getCode()).setMessage(ex.getMsg()).setData(null);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JsonResult<Object> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        String msg = "MethodArgumentNotValidException error";
        log.error(msg, ex);
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            sb.append(errorMessage).append(". ");
        });
        return new JsonResult<>().setSuccess(false).setCode(400).setMessage(sb.toString()).setData(null);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public JsonResult<Object> handleException(Exception ex) {
        String msg = "Exception error";
        log.error(msg, ex);
        return new JsonResult<>().setSuccess(false).setCode(500).setMessage(ex.getMessage()).setData(null);
    }


}

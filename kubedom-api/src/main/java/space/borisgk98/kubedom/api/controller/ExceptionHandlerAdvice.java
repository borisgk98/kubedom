package space.borisgk98.kubedom.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import space.borisgk98.kubedom.api.security.InvalidJwtAuthenticationException;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ResponseStatus(value= HttpStatus.FORBIDDEN,
            reason="Expired or invalid JWT token")  // 403
    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public void invalidToken() {
        // Nothing to do
    }
}

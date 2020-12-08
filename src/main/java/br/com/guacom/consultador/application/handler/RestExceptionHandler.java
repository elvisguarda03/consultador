package br.com.guacom.consultador.application.handler;

import br.com.guacom.consultador.domain.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, Object>> resourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> message = new HashMap<>();
        message.put("error", ex.getMessage());
        message.put("status", HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> validationFailed(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        Map<String, Object> message = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> errors.add(error.getDefaultMessage()));

        message.put("errors", errors);
        message.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}

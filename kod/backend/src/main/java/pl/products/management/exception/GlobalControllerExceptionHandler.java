package pl.products.management.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<String> handleInvalidInput(RuntimeException gotException){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gotException.getMessage());
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<String> handleNotFound(RuntimeException gotException){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gotException.getMessage());
    }

    @ExceptionHandler(value = {EntityExistsException.class})
    public ResponseEntity<String> handleConflict(RuntimeException gotException){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(gotException.getMessage());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, String>> handleForm(MethodArgumentNotValidException gotException){

        Map<String, String> errors = new HashMap<>();

        for(FieldError fieldError : gotException.getBindingResult().getFieldErrors()){

            String fieldName = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();

            errors.put(fieldName, errorMessage);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}

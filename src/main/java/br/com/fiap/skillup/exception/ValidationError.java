package br.com.fiap.skillup.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ValidationError extends StandardError {

    private List<FieldErrorMessage> errors;

    public ValidationError(LocalDateTime timestamp, Integer status, String message) {
        super(timestamp, status, message);
    }

    public List<FieldErrorMessage> getErrors() { return errors; }
    public void setErrors(List<FieldErrorMessage> errors) { this.errors = errors; }
}

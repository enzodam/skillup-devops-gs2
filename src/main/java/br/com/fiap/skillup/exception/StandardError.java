package br.com.fiap.skillup.exception;

import java.time.LocalDateTime;

public class StandardError {

    private LocalDateTime timestamp;
    private Integer status;
    private String message;

    public StandardError(LocalDateTime timestamp, Integer status, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

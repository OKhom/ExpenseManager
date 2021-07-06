package com.okdev.ems.dto;

public class TokenValidateDTO {
    private Boolean success;
    private String message;

    public TokenValidateDTO() {
    }

    public TokenValidateDTO(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static TokenValidateDTO of(Boolean success, String message) {
        return new TokenValidateDTO(success, message);
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

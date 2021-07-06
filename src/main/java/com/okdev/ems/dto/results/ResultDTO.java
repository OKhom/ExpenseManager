package com.okdev.ems.dto.results;

public abstract class ResultDTO {
    protected Boolean success = true;

    public ResultDTO() {
    }

    public ResultDTO(Boolean success) {
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}

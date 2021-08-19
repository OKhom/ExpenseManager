package com.okdev.ems.dto.results;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object for Result of Request")
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

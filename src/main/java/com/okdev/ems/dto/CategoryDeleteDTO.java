package com.okdev.ems.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object for Mode of Deleting Category w/wo Transactions")
public class CategoryDeleteDTO {
    private Boolean deleteWithAllTransaction;

    public CategoryDeleteDTO() {
    }

    public CategoryDeleteDTO(Boolean deleteWithAllTransaction) {
        this.deleteWithAllTransaction = deleteWithAllTransaction;
    }

    public Boolean getDeleteWithAllTransaction() {
        return deleteWithAllTransaction;
    }

    public void setDeleteWithAllTransaction(Boolean deleteWithAllTransaction) {
        this.deleteWithAllTransaction = deleteWithAllTransaction;
    }
}

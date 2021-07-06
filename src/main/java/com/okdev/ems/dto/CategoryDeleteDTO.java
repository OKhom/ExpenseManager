package com.okdev.ems.dto;

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

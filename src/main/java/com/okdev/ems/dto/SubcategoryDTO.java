package com.okdev.ems.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object for Subcategory Entity")
public class SubcategoryDTO {
    private Long subcategoryId;
    private String subname;

    public SubcategoryDTO() {
    }

    public SubcategoryDTO(Long subcategoryId, String subname) {
        this.subcategoryId = subcategoryId;
        this.subname = subname;
    }

    public static SubcategoryDTO of(Long subcategoryId, String subname) {
        return new SubcategoryDTO(subcategoryId, subname);
    }

    public Long getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(Long subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }
}

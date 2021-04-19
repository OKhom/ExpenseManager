package com.okdev.ems.dto;

public class SubcategoryDTOext extends SubcategoryDTO {
    private Long userId;
    private Long categoryId;

    public SubcategoryDTOext() {
    }

    public SubcategoryDTOext(Long userId, Long categoryId, Long subcategoryId, String subname) {
        this.userId = userId;
        this.categoryId = categoryId;
        super.setSubcategoryId(subcategoryId);
        super.setSubname(subname);
    }

    public static SubcategoryDTOext of(Long userId, Long categoryId, Long subcategoryId, String subname) {
        return new SubcategoryDTOext(userId, categoryId, subcategoryId, subname);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public Long getSubcategoryId() {
        return super.getSubcategoryId();
    }

    @Override
    public void setSubcategoryId(Long subcategoryId) {
        super.setSubcategoryId(subcategoryId);
    }

    @Override
    public String getSubname() {
        return super.getSubname();
    }

    @Override
    public void setSubname(String subname) {
        super.setSubname(subname);
    }
}

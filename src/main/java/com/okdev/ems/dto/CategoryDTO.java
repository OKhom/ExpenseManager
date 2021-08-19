package com.okdev.ems.dto;

import com.okdev.ems.models.enums.CategoryType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Data Transfer Object for Category Entity")
public class CategoryDTO {
    private Long userId;
    private Long categoryId;
    private CategoryType type;
    private String name;
    private String description;
    private Long currencyId;
    private String currencySign;
    private Double totalExpense;
    private Double budget;
    private List<SubcategoryDTO> subcategories;

    public CategoryDTO() {
    }

    public CategoryDTO(Long userId, Long categoryId, CategoryType type, String name, String description, Long currencyId,
                       String currencySign, Double totalExpense, Double budget, List<SubcategoryDTO> subcategories) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.type = type;
        this.name = name;
        this.description = description;
        this.currencyId = currencyId;
        this.currencySign = currencySign;
        this.totalExpense = totalExpense;
        this.budget = budget;
        this.subcategories = subcategories;
    }

    public static CategoryDTO of(Long userId, Long categoryId, CategoryType type, String name, String description, Long currencyId,
                                 String currencySign, Double totalExpense, Double budget, List<SubcategoryDTO> subcategories) {
        return new CategoryDTO(userId, categoryId, type, name, description, currencyId, currencySign, totalExpense, budget, subcategories);
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

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SubcategoryDTO> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<SubcategoryDTO> subcategories) {
        this.subcategories = subcategories;
    }

    public Double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(Double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencySign() {
        return currencySign;
    }

    public void setCurrencySign(String currencySign) {
        this.currencySign = currencySign;
    }
}

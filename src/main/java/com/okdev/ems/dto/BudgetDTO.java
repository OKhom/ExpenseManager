package com.okdev.ems.dto;

public class BudgetDTO {
    private Long userId;
    private Long categoryId;
    private Integer year;
    private Integer month;
    private Double budget;

    public BudgetDTO() {
    }

    public BudgetDTO(Long userId, Long categoryId, Integer year, Integer month, Double budget) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.year = year;
        this.month = month;
        this.budget = budget;
    }

    public static BudgetDTO of(Long userId, Long categoryId, Integer year, Integer month, Double budget) {
        return new BudgetDTO(userId, categoryId, year, month, budget);
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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }
}

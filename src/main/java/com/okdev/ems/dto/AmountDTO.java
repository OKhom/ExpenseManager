package com.okdev.ems.dto;

public class AmountDTO {
    private Long userId;
    private Long currencyId;
    private Integer year;
    private Integer month;
    private String currencySign;
    private Double amountIncome;
    private Double amountExpense;

    public AmountDTO() {
    }

    public AmountDTO(Long userId, Long currencyId, Integer year, Integer month, String currencySign, Double amountIncome, Double amountExpense) {
        this.userId = userId;
        this.currencyId = currencyId;
        this.year = year;
        this.month = month;
        this.currencySign = currencySign;
        this.amountIncome = amountIncome;
        this.amountExpense = amountExpense;
    }

    public static AmountDTO of(Long userId, Long currencyId, Integer year, Integer month, String currencySign, Double amountIncome, Double amountExpense) {
        return new AmountDTO(userId, currencyId, year, month, currencySign, amountIncome, amountExpense);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
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

    public String getCurrencySign() {
        return currencySign;
    }

    public void setCurrencySign(String currencySign) {
        this.currencySign = currencySign;
    }

    public Double getAmountIncome() {
        return amountIncome;
    }

    public void setAmountIncome(Double amountIncome) {
        this.amountIncome = amountIncome;
    }

    public Double getAmountExpense() {
        return amountExpense;
    }

    public void setAmountExpense(Double amountExpense) {
        this.amountExpense = amountExpense;
    }
}

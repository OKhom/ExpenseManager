package com.okdev.ems.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.okdev.ems.models.enums.CategoryType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Data Transfer Object for Transaction Entity")
public class TransactionDTO {
    private Long userId;
    private Long categoryId;
    private Long transactionId;
    private Long subcategoryId;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String categoryName;
    private Double amount;
    private String currencySign;
    private String subcategory;
    private String note;
    private CategoryType type;

    public TransactionDTO() {
    }

    public TransactionDTO(Long userId, Long categoryId, Long transactionId, LocalDate date,
                          String categoryName, Double amount, String currencySign, String note, CategoryType type) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.transactionId = transactionId;
        this.date = date;
        this.categoryName = categoryName;
        this.amount = amount;
        this.currencySign = currencySign;
        this.note = note;
        this.type = type;
    }

    public TransactionDTO(Long userId, Long categoryId, Long transactionId, Long subcategoryId, LocalDate date,
                          String categoryName, Double amount, String currencySign, String subcategory, String note, CategoryType type) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.transactionId = transactionId;
        this.subcategoryId = subcategoryId;
        this.date = date;
        this.categoryName = categoryName;
        this.amount = amount;
        this.currencySign = currencySign;
        this.subcategory = subcategory;
        this.note = note;
        this.type = type;
    }

    public static TransactionDTO of(Long userId, Long categoryId, Long transactionId, LocalDate date,
                                    String categoryName, Double amount, String currencySign, String note, CategoryType type) {
        return new TransactionDTO(userId, categoryId, transactionId, date, categoryName, amount, currencySign, note, type);
    }

    public static TransactionDTO of(Long userId, Long categoryId, Long transactionId, Long subcategoryId, LocalDate date,
                                    String categoryName, Double amount, String currencySign, String subcategory, String note, CategoryType type) {
        return new TransactionDTO(userId, categoryId, transactionId, subcategoryId, date, categoryName, amount, currencySign, subcategory, note, type);
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

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCurrencySign() {
        return currencySign;
    }

    public void setCurrencySign(String currencySign) {
        this.currencySign = currencySign;
    }

    public Long getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(Long subcategoryId) {
        this.subcategoryId = subcategoryId;
    }
}

package com.okdev.ems.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class TransactionDTO {
    Long userId;
    Long categoryId;
    Long transactionId;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate date;

    Double amount;
    String subcategory;
    String note;

    public TransactionDTO() {
    }

    public TransactionDTO(Long userId, Long categoryId, Long transactionId, LocalDate date, Double amount, String note) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.transactionId = transactionId;
        this.date = date;
        this.amount = amount;
        this.note = note;
    }

    public TransactionDTO(Long userId, Long categoryId, Long transactionId, LocalDate date, Double amount, String subcategory, String note) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.transactionId = transactionId;
        this.date = date;
        this.amount = amount;
        this.subcategory = subcategory;
        this.note = note;
    }

    public static TransactionDTO of(Long userId, Long categoryId, Long transactionId, LocalDate date, Double amount, String note) {
        return new TransactionDTO(userId, categoryId, transactionId, date, amount, note);
    }

    public static TransactionDTO of(Long userId, Long categoryId, Long transactionId, LocalDate date, Double amount, String subcategory, String note) {
        return new TransactionDTO(userId, categoryId, transactionId, date, amount, subcategory, note);
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
}

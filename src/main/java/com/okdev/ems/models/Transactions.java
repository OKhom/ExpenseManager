package com.okdev.ems.models;

import com.okdev.ems.dto.TransactionDTO;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Transactions {
    @Id
    @GeneratedValue
    private Long transactionId;

//    @Temporal(TemporalType.DATE)
    private LocalDate date;

    private Double amount;
    private String note;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Categories category;

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private Subcategories subcategory;

    public Transactions() {
    }

    public Transactions(LocalDate date, Double amount, String note, Categories category) {
        this.date = date;
        this.amount = amount;
        this.note = note;
        this.category = category;
    }

    public Transactions(LocalDate date, Double amount, String note, Categories category, Subcategories subcategory) {
        this.date = date;
        this.amount = amount;
        this.note = note;
        this.category = category;
        this.subcategory = subcategory;
    }

    public static Transactions of(Categories category, LocalDate date, Double amount, String note) {
        return new Transactions(date, amount, note, category);
    }

    public static Transactions of(Categories category, LocalDate date, Double amount, Subcategories subcategory, String note) {
        return new Transactions(date, amount, note, category, subcategory);
    }

    public static Transactions fromDTO(Categories category, TransactionDTO transactionDTO) {
        return Transactions.of(category, transactionDTO.getDate(), transactionDTO.getAmount(), transactionDTO.getNote());
    }

    public static Transactions fromDTO(Categories category, Subcategories subcategory, TransactionDTO transactionDTO) {
        return Transactions.of(category, transactionDTO.getDate(), transactionDTO.getAmount(), subcategory, transactionDTO.getNote());
    }

    public TransactionDTO toDTO() {
        if (subcategory == null)
            return TransactionDTO.of(category.getUser().getUserId(), category.getCategoryId(), transactionId, date, amount, note);
        else
            return TransactionDTO.of(category.getUser().getUserId(), category.getCategoryId(), transactionId, date, amount, subcategory.getSubname(), note);
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }

    public Subcategories getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategories subcategory) {
        this.subcategory = subcategory;
    }
}

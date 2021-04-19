package com.okdev.ems.models;

import com.okdev.ems.dto.CategoryDTO;
import com.okdev.ems.dto.SubcategoryDTO;
import com.okdev.ems.models.enums.CategoryType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Categories {
    @Id
    @GeneratedValue
    private Long categoryId;
    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private CategoryType type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Subcategories> subcategories = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Transactions> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Budgets> budgets = new ArrayList<>();

    public Categories() {
    }

    public Categories(Users user, String name, String description, CategoryType type) {
        this.name = name;
        this.description = description;
        this.user = user;
        this.type = type;
    }

    public static Categories of(Users user, String name, String description, CategoryType type) {
        return new Categories(user, name, description, type);
    }

    public static Categories fromDTO(Users user, CategoryDTO categoryDTO, CategoryType type) {
        return Categories.of(user, categoryDTO.getName(), categoryDTO.getDescription(), type);
    }

    public CategoryDTO toDTO() {
        Double totalExpense = transactions.stream().mapToDouble(Transactions::getAmount).sum();
        List<SubcategoryDTO> subcategory = subcategories.stream().map(Subcategories::toDTO).collect(Collectors.toList());
        return CategoryDTO.of(user.getUserId(), categoryId, type, name, description, totalExpense, subcategory);
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long id) {
        this.categoryId = id;
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

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<Subcategories> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategories> subcategories) {
        this.subcategories = subcategories;
    }

    public List<Transactions> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transactions> transactions) {
        this.transactions = transactions;
    }

    public List<Budgets> getBudgets() {
        return budgets;
    }

    public void setBudgets(List<Budgets> budgets) {
        this.budgets = budgets;
    }
}

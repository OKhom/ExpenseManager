package com.okdev.ems.models;

import com.okdev.ems.dto.CategoryDTO;
import com.okdev.ems.dto.SubcategoryDTO;
import com.okdev.ems.models.enums.CategoryType;
import com.okdev.ems.repositories.CurrencyRepository;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private CategoryType type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currencies currency;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Subcategories> subcategories = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Transactions> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Budgets> budgets = new ArrayList<>();

    public Categories() {
    }

    public Categories(Users user, String name, String description, CategoryType type, Currencies currency) {
        this.name = name;
        this.description = description;
        this.user = user;
        this.type = type;
        this.currency = currency;
    }

    public static Categories of(Users user, String name, String description, CategoryType type, Currencies currency) {
        return new Categories(user, name, description, type, currency);
    }

    public static Categories fromDTO(Users user, CategoryDTO categoryDTO, CategoryType type, Currencies currency) {
        return Categories.of(user, categoryDTO.getName(), categoryDTO.getDescription(), type, currency);
    }

    public CategoryDTO toDTO() {
        Double totalExpense = transactions.stream().mapToDouble(Transactions::getAmount).sum();
        Double budget = budgets.stream().mapToDouble(Budgets::getBudget).sum();
        List<SubcategoryDTO> subcategory = getListSubcategories();
        return CategoryDTO.of(user.getUserId(), categoryId, type, name, description,
                currency.getCurrencyId(), currency.getSign(), totalExpense, budget, subcategory);
    }

    public CategoryDTO toDTObyDate(LocalDate currentDate) {
        Double totalExpense = getMonthExpense(currentDate);
        Double budget = getMonthBudget(currentDate);
        List<SubcategoryDTO> subcategory = getListSubcategories();
        return CategoryDTO.of(user.getUserId(), categoryId, type, name, description,
                currency.getCurrencyId(), currency.getSign(), totalExpense, budget, subcategory);
    }

    private Double getMonthExpense(LocalDate currentDate) {
        return transactions.stream()
                .filter(t -> t.getDate().isAfter(currentDate.minusDays(1)))
                .filter(t -> t.getDate().isBefore(currentDate.plusMonths(1)))
                .mapToDouble(Transactions::getAmount)
                .sum();
    }

    private Double getMonthBudget(LocalDate currentDate) {
        return budgets.stream()
                .filter(b -> b.getCategory().getCategoryId().equals(categoryId))
                .filter(b -> b.getBudgetId().getYear().equals(currentDate.getYear()))
                .filter(b -> b.getBudgetId().getMonth().equals(currentDate.getMonthValue()))
                .mapToDouble(Budgets::getBudget)
                .sum();
    }

    private List<SubcategoryDTO> getListSubcategories() {
        return subcategories.stream()
                .map(Subcategories::toDTO)
                .collect(Collectors.toList());
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

    public Currencies getCurrency() {
        return currency;
    }

    public void setCurrency(Currencies currency) {
        this.currency = currency;
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

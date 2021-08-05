package com.okdev.ems.models;

import com.okdev.ems.dto.BudgetDTO;
import com.okdev.ems.models.embeddedID.BudgetId;

import javax.persistence.*;

@Entity
public class Budgets {

    @EmbeddedId
    private BudgetId budgetId;
    private Double budget;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Categories category;

    public Budgets() {
    }

    public Budgets(BudgetId budgetId, Categories category, Double budget) {
        this.budgetId = budgetId;
        this.budget = budget;
        this.category = category;
    }

    public static Budgets of(Categories category, Integer year, Integer month, Double budget) {
        BudgetId budgetId = new BudgetId(category.getCategoryId(), year, month);
        return new Budgets(budgetId, category, budget);
    }

    public static Budgets fromDTO(Categories category, BudgetDTO budgetDTO) {
        return Budgets.of(category, budgetDTO.getYear(), budgetDTO.getMonth(), budgetDTO.getBudget());
    }

    public BudgetDTO toDTO() {
        return BudgetDTO.of(category.getUser().getUserId(), category.getCategoryId(), budgetId.getYear(), budgetId.getMonth(), budget);
    }

    public BudgetId getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(BudgetId budgetId) {
        this.budgetId = budgetId;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }
}

package com.okdev.ems.models.embeddedID;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class BudgetId implements Serializable {
    private Long category;
    private Integer year;
    private Integer month;

    public BudgetId() {
    }

    public BudgetId(Long category, Integer year, Integer month) {
        this.category = category;
        this.year = year;
        this.month = month;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
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
}

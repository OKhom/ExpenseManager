package com.okdev.ems.models;

import com.okdev.ems.dto.SubcategoryDTO;
import com.okdev.ems.dto.SubcategoryDTOext;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Subcategories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subcategoryId;
    private String subname;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Categories category;

    @OneToMany(mappedBy = "subcategory", cascade = CascadeType.ALL)
    private List<Transactions> transactions = new ArrayList<>();

    public Subcategories() {
    }

    public Subcategories(Categories category, String subname) {
        this.category = category;
        this.subname = subname;
    }

    public static Subcategories of(Categories category, String subname) {
        return new Subcategories(category, subname);
    }

    public static Subcategories fromDTO(Categories category, SubcategoryDTO subcategoryDTO) {
        return Subcategories.of(category, subcategoryDTO.getSubname());
    }

    public SubcategoryDTOext toDTOext() {
        return SubcategoryDTOext.of(category.getUser().getUserId(), category.getCategoryId(), subcategoryId, subname);
    }

    public SubcategoryDTO toDTO() {
        return SubcategoryDTO.of(subcategoryId, subname);
    }

    public void setSubcategoryId(Long subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public Long getSubcategoryId() {
        return subcategoryId;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }

    public List<Transactions> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transactions> transactions) {
        this.transactions = transactions;
    }

}

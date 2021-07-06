package com.okdev.ems.models;

import com.okdev.ems.dto.CurrencyDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Currencies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long currencyId;
    private String name;
    private String shortName;
    private String sign;

    @OneToMany(mappedBy = "currency", cascade = CascadeType.ALL)
    private List<Categories> category = new ArrayList<>();

    @OneToMany(mappedBy = "currency", cascade = CascadeType.ALL)
    private List<Users> users = new ArrayList<>();

    public Currencies() {
    }

    public Currencies(String name, String shortName, String sign) {
        this.name = name;
        this.shortName = shortName;
        this.sign = sign;
    }

    public CurrencyDTO toDTO() {
        return CurrencyDTO.of(currencyId, name, shortName, sign);
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public List<Categories> getCategory() {
        return category;
    }

    public void setCategory(List<Categories> category) {
        this.category = category;
    }

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }
}

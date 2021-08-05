package com.okdev.ems.models;

import com.okdev.ems.dto.AmountDTO;
import com.okdev.ems.dto.UserDTO;
import com.okdev.ems.models.enums.UserRole;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currencies currency;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Categories> category = new ArrayList<>();

    public Users() {
    }

    public Users(String firstName, String lastName, String email, String password, UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Users(String firstName, String lastName, String email, String password, UserRole role, Currencies currency) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.currency = currency;
    }

    public UserDTO toDTO() {
        Long currencyId = currency == null ? null : currency.getCurrencyId();
        String currencyName = currency == null ? "No currency" : currency.getName();
        String currencyShortName = currency == null ? "" : currency.getShortName();
        return UserDTO.of(userId, currencyId, firstName, lastName, email, currencyName, currencyShortName, role);
    }

    public AmountDTO toAmountDTO(LocalDate currentDate, Double amountIncome, Double amountExpense) {
        Long currencyId = currency == null ? null : currency.getCurrencyId();
        String currencySign = currency == null ? "N/A" : currency.getSign();
        return AmountDTO.of(userId, currencyId, currentDate.getYear(), currentDate.getMonthValue(), currencySign, amountIncome, amountExpense);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long id) {
        this.userId = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Currencies getCurrency() {
        return currency;
    }

    public void setCurrency(Currencies currency) {
        this.currency = currency;
    }

    public List<Categories> getCategory() {
        return category;
    }

    public void setCategory(List<Categories> category) {
        this.category = category;
    }
}

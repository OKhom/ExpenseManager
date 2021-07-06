package com.okdev.ems.dto;

public class CurrencyDTO {
    private Long currencyId;
    private String name;
    private String shortName;
    private String sign;

    public CurrencyDTO() {
    }

    public CurrencyDTO(Long currencyId, String name, String shortName, String sign) {
        this.currencyId = currencyId;
        this.name = name;
        this.shortName = shortName;
        this.sign = sign;
    }

    public static CurrencyDTO of(Long currencyId, String name, String shortName, String sign) {
        return new CurrencyDTO(currencyId, name, shortName, sign);
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
}

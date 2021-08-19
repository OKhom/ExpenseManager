package com.okdev.ems.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object for Count of Users and Currencies Pages (Admin)")
public class PageCountDTO {
    private final Long count;
    private final Long countCurrencies;
    private final Integer pageSize;

    public PageCountDTO(Long count, Long countCurrencies, Integer pageSize) {
        this.count = count;
        this.countCurrencies = countCurrencies;
        this.pageSize = pageSize;
    }

    public static PageCountDTO of(Long count, Long countCurrencies, Integer pageSize) {
        return new PageCountDTO(count, countCurrencies, pageSize);
    }

    public Long getCount() {
        return count;
    }

    public Long getCountCurrencies() {
        return countCurrencies;
    }

    public Integer getPageSize() {
        return pageSize;
    }
}

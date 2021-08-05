package com.okdev.ems.dto;

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

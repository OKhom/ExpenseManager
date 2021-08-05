package com.okdev.ems.services;

import com.okdev.ems.dto.CurrencyDTO;
import com.okdev.ems.exceptions.EmsBadRequestException;
import com.okdev.ems.exceptions.EmsResourceNotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CurrencyService {

    List<CurrencyDTO> fetchAllCurrencies();

    List<CurrencyDTO> fetchAllCurrencies(Pageable pageable);

    List<CurrencyDTO> fetchByPattern(String pattern);

    CurrencyDTO fetchCurrencyById(Long currencyId) throws EmsResourceNotFoundException;

    Long countAllCurrencies() throws EmsResourceNotFoundException;

    CurrencyDTO addCurrency(CurrencyDTO currencyDTO) throws EmsBadRequestException;

    CurrencyDTO editCurrency(Long currencyId, CurrencyDTO currencyDTO) throws EmsBadRequestException;

    void deleteCurrencies(List<Long> currenciesId) throws EmsResourceNotFoundException;
}

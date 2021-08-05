package com.okdev.ems.services;

import com.okdev.ems.dto.CurrencyDTO;
import com.okdev.ems.exceptions.EmsBadRequestException;
import com.okdev.ems.exceptions.EmsResourceNotFoundException;
import com.okdev.ems.models.Currencies;
import com.okdev.ems.repositories.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    CurrencyRepository currencyRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CurrencyDTO> fetchAllCurrencies() {
        return currencyRepository.findAll().stream().map(Currencies::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CurrencyDTO> fetchAllCurrencies(Pageable pageable) {
        return currencyRepository.findAll(pageable).stream().map(Currencies::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CurrencyDTO> fetchByPattern(String pattern) {
        return currencyRepository.findByPattern(pattern).stream().map(Currencies::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CurrencyDTO fetchCurrencyById(Long currencyId) throws EmsResourceNotFoundException {
        try {
            return currencyRepository.getOne(currencyId).toDTO();
        } catch (NullPointerException npe) {
            throw new EmsResourceNotFoundException("Currency ID not found");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long countAllCurrencies() throws EmsResourceNotFoundException {
        try {
            return currencyRepository.countAllCurrencies();
        } catch (NullPointerException npe) {
            throw new EmsResourceNotFoundException("Currencies not found");
        }
    }

    @Override
    @Transactional
    public CurrencyDTO addCurrency(CurrencyDTO currencyDTO) throws EmsBadRequestException {
        try {
            Currencies currency = Currencies.fromDTO(currencyDTO);
            currencyRepository.save(currency);
            return currency.toDTO();
        } catch (NullPointerException npe) {
            throw new EmsBadRequestException("Add Currency: invalid request");
        } catch (Exception e) {
            throw new EmsResourceNotFoundException("Add Currency: element not found in DB");
        }
    }

    @Override
    @Transactional
    public CurrencyDTO editCurrency(Long currencyId, CurrencyDTO currencyDTO) throws EmsBadRequestException {
        try {
            Currencies currency = currencyRepository.getOne(currencyId);
            if (currencyDTO.getName() != null)
                currency.setName(currencyDTO.getName());
            if (currencyDTO.getShortName() != null)
                currency.setShortName(currencyDTO.getShortName());
            if (currencyDTO.getSign() != null)
                currency.setSign(currencyDTO.getSign());
            currencyRepository.save(currency);
            return currency.toDTO();
        } catch (NullPointerException npe) {
            throw new EmsBadRequestException("Edit Currency: invalid request");
        }
    }


    @Override
    @Transactional
    public void deleteCurrencies(List<Long> currenciesId) throws EmsResourceNotFoundException {
        currenciesId.forEach((c) -> currencyRepository.deleteById(c));
    }
}

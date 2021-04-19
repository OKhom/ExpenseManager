package com.okdev.ems.services;

import com.okdev.ems.dto.BudgetDTO;
import com.okdev.ems.exceptions.EmsBadRequestException;
import com.okdev.ems.exceptions.EmsResourceNotFoundException;

import java.util.List;

public interface BudgetService {

    List<BudgetDTO> fetchAllBudgets(Long userId, Long categoryId);

    BudgetDTO fetchBudgetByDate(Long userId, Long categoryId, Integer year, Integer month) throws EmsResourceNotFoundException;

    BudgetDTO addBudget(Long userId, Long categoryId, BudgetDTO budgetDTO) throws EmsBadRequestException;

    BudgetDTO updateBudget(Long userId, Long categoryId, Integer year, Integer month, BudgetDTO budgetDTO) throws EmsBadRequestException;

    void removeBudget(Long userId, Long categoryId, Integer year, Integer month) throws EmsResourceNotFoundException;
}

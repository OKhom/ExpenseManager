package com.okdev.ems.services;

import com.okdev.ems.dto.BudgetDTO;
import com.okdev.ems.exceptions.EmsBadRequestException;
import com.okdev.ems.exceptions.EmsResourceNotFoundException;
import com.okdev.ems.models.Budgets;
import com.okdev.ems.models.Categories;
import com.okdev.ems.repositories.BudgetRepository;
import com.okdev.ems.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetServiceImpl implements BudgetService{

    private final CategoryRepository categoryRepository;
    private final BudgetRepository budgetRepository;

    @Autowired
    public BudgetServiceImpl(CategoryRepository categoryRepository, BudgetRepository budgetRepository) {
        this.categoryRepository = categoryRepository;
        this.budgetRepository = budgetRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BudgetDTO> fetchAllBudgets(Long userId, Long categoryId) {
        return budgetRepository.findAll(userId, categoryId).stream().map(Budgets::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BudgetDTO fetchBudgetByDate(Long userId, Long categoryId, Integer year, Integer month) throws EmsResourceNotFoundException {
        try {
            return budgetRepository.findByDate(userId, categoryId, year, month).toDTO();
        } catch (NullPointerException npe) {
            throw new EmsResourceNotFoundException("Budget ID not found");
        }
    }

    @Override
    @Transactional
    public BudgetDTO addBudget(Long userId, Long categoryId, BudgetDTO budgetDTO) throws EmsBadRequestException {
        try {
            Categories category = categoryRepository.findById(userId, categoryId);
            if (budgetRepository.findByDate(userId, categoryId, budgetDTO.getYear(), budgetDTO.getMonth()) == null) {
                Budgets budget = Budgets.fromDTO(category, budgetDTO);
                budgetRepository.save(budget);
                return budget.toDTO();
            } else throw new EmsBadRequestException("Add Budget: duplicated unique keys");
        } catch (NullPointerException npe) {
            throw new EmsBadRequestException("Add Budget: invalid request");
        }
    }

    @Override
    @Transactional
    public BudgetDTO updateBudget(Long userId, Long categoryId, Integer year, Integer month, BudgetDTO budgetDTO) throws EmsBadRequestException {
        try {
            Budgets budget = budgetRepository.findByDate(userId, categoryId, year, month);
            if (budgetDTO.getBudget() != null)
                budget.setBudget(budgetDTO.getBudget());
            budgetRepository.save(budget);
            return budget.toDTO();
        } catch (NullPointerException npe) {
            throw new EmsBadRequestException("Update Budget: invalid request");
        }
    }

    @Override
    @Transactional
    public void removeBudget(Long userId, Long categoryId, Integer year, Integer month) throws EmsResourceNotFoundException {
        try {
            Budgets budget = budgetRepository.findByDate(userId, categoryId, year, month);
            budgetRepository.deleteById(budget.getBudgetId());
        } catch (NullPointerException npe) {
            throw new EmsResourceNotFoundException("Delete Budget: invalid request");
        }
    }
}

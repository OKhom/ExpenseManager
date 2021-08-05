package com.okdev.ems.services;

import com.okdev.ems.config.utils.ExchangeRates;
import com.okdev.ems.dto.*;
import com.okdev.ems.exceptions.EmsBadRequestException;
import com.okdev.ems.exceptions.EmsResourceNotFoundException;
import com.okdev.ems.models.*;
import com.okdev.ems.models.enums.CategoryType;
import com.okdev.ems.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    SubcategoryRepository subcategoryRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CurrencyRepository currencyRepository;

    @Autowired
    ExchangeRates exchangeRates;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> fetchAllCategories(Long userId) {
        return categoryRepository.findAll(userId).stream().map(Categories::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> fetchCategoriesByTypeAndDate(Long userId, Integer year, Integer month, CategoryType type) {
        LocalDate currentMonth = LocalDate.of(year, month, 1);
        return categoryRepository.findByType(userId, type).stream().map(c -> c.toDTObyDate(currentMonth)).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO fetchCategoryById(Long userId, Long categoryId) throws EmsResourceNotFoundException {
        try {
            return categoryRepository.findById(userId, categoryId).toDTO();
        } catch (NullPointerException npe) {
            throw new EmsResourceNotFoundException("Category ID not found");
        }
    }

    @Override
    @Transactional
    public CategoryDTO addCategory(Long userId, CategoryDTO categoryDTO, CategoryType type) throws EmsBadRequestException {
        try {
            Users user = userRepository.findUserById(userId);
            Currencies currency = currencyRepository.getOne(categoryDTO.getCurrencyId());
            Categories category = Categories.fromDTO(user, categoryDTO, type, currency);
            categoryRepository.save(category);
            return category.toDTO();
        } catch (NullPointerException npe) {
            throw new EmsBadRequestException("Add Category: invalid request");
        } catch (Exception e) {
            throw new EmsResourceNotFoundException("Add Category: element not found in DB");
        }
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long userId, Long categoryId, CategoryDTO categoryDTO) throws EmsBadRequestException {
        try {
            Categories category = categoryRepository.findById(userId, categoryId);
            if (categoryDTO.getName() != null)
                category.setName(categoryDTO.getName());
            if (categoryDTO.getDescription() != null)
                category.setDescription(categoryDTO.getDescription());
            if (categoryDTO.getCurrencyId() != null)
                category.setCurrency(currencyRepository.getOne(categoryDTO.getCurrencyId()));
            categoryRepository.save(category);
            return category.toDTO();
        } catch (NullPointerException npe) {
            throw new EmsBadRequestException("Update Category: invalid request");
        }
    }

    @Override
    @Transactional
    public void removeCategory(Long userId, Long categoryId, CategoryDeleteDTO categoryDeleteDTO) throws EmsResourceNotFoundException {
        try {
            Categories category = categoryRepository.findById(userId, categoryId);
            if (!categoryDeleteDTO.getDeleteWithAllTransaction() && category.getTransactions().size() > 0)
                throw new EmsBadRequestException("Category deleting error: associated with Transactions");
            categoryRepository.deleteById(category.getCategoryId());
        } catch (NullPointerException npe) {
            throw new EmsResourceNotFoundException("Delete Category: invalid request");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubcategoryDTOext> fetchAllSubcategories(Long userId, Long categoryId) {
        return subcategoryRepository.findAll(userId, categoryId).stream().map(Subcategories::toDTOext).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SubcategoryDTOext fetchSubcategoryById(Long userId, Long categoryId, Long subcategoryId) throws EmsResourceNotFoundException {
        try {
            return subcategoryRepository.findById(userId, categoryId, subcategoryId).toDTOext();
        } catch (NullPointerException npe) {
            throw new EmsResourceNotFoundException("Subcategory ID not found");
        }
    }

    @Override
    @Transactional
    public SubcategoryDTOext addSubcategory(Long userId, Long categoryId, SubcategoryDTO subcategoryDTO) throws EmsBadRequestException {
        try {
            Categories category = categoryRepository.findById(userId, categoryId);
            Subcategories subcategory = Subcategories.fromDTO(category, subcategoryDTO);
            subcategoryRepository.save(subcategory);
            return subcategory.toDTOext();
        } catch (NullPointerException npe) {
            throw new EmsBadRequestException("Add Subcategory: invalid request");
        }
    }

    @Override
    @Transactional
    public SubcategoryDTOext updateSubcategory(Long userId, Long categoryId, Long subcategoryId, SubcategoryDTO subcategoryDTO) throws EmsBadRequestException {
        try {
            Subcategories subcategory = subcategoryRepository.findById(userId, categoryId, subcategoryId);
            if (subcategoryDTO.getSubname() != null)
                subcategory.setSubname(subcategoryDTO.getSubname());
            subcategoryRepository.save(subcategory);
            return subcategory.toDTOext();
        } catch (NullPointerException npe) {
            throw new EmsBadRequestException("Update Subcategory: invalid request");
        }
    }

    @Override
    @Transactional
    public void removeSubcategory(Long userId, Long categoryId, Long subcategoryId) throws EmsResourceNotFoundException {
        try {
            Subcategories subcategory = subcategoryRepository.findById(userId, categoryId, subcategoryId);
            if (subcategory.getTransactions().size() > 0)
                throw new EmsBadRequestException("Subcategory deleting error: associated with Transactions");
            subcategoryRepository.deleteById(subcategory.getSubcategoryId());
        } catch (NullPointerException npe) {
            throw new EmsResourceNotFoundException("Delete Subcategory: invalid request");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AmountDTO getTotalAmountByDate(Long userId, Integer year, Integer month) throws EmsResourceNotFoundException {
        Users user = userRepository.findUserById(userId);
        LocalDate currentMonth = LocalDate.of(year, month, 1);
        Double amountIncome = amount(user, CategoryType.INCOME, currentMonth);
        Double amountExpense = amount(user, CategoryType.EXPENSE, currentMonth);
        return user.toAmountDTO(currentMonth, amountIncome, amountExpense);
    }

    @Override
    @Transactional(readOnly = true)
    public Double amount(Users user, CategoryType type, LocalDate currentMonth) {
        Double amount = 0D;
        if (user.getCurrency() != null) {
            String currencyDefault = user.getCurrency().getShortName();
            List<CategoryDTO> categoryList = categoryRepository.findByType(user.getUserId(), type).stream()
                    .map(c -> c.toDTObyDate(currentMonth))
                    .collect(Collectors.toList());
            try {
                for (CategoryDTO category : categoryList) {
                    Double currencyRate;
                    if (!user.getCurrency().getCurrencyId().equals(category.getCurrencyId())) {
                        currencyRate = exchangeRates.currentRate(currencyDefault, currencyRepository.getOne(category.getCurrencyId()).getShortName());
                    } else {
                        currencyRate = 1D;
                    }
                    amount += category.getTotalExpense() * currencyRate;
                }
            } catch (IOException e) {
                throw new EmsBadRequestException("Currency Rate request is invalid");
            }
        }
        return amount;
    }
}

package com.okdev.ems.services;

import com.okdev.ems.dto.*;
import com.okdev.ems.exceptions.EmsBadRequestException;
import com.okdev.ems.exceptions.EmsResourceNotFoundException;
import com.okdev.ems.models.Users;
import com.okdev.ems.models.enums.CategoryType;

import java.time.LocalDate;
import java.util.List;

public interface CategoryService {

    List<CategoryDTO> fetchAllCategories(Long userId);

    List<CategoryDTO> fetchCategoriesByTypeAndDate(Long userId, Integer year, Integer month, CategoryType type);

    CategoryDTO fetchCategoryById(Long userId, Long categoryId) throws EmsResourceNotFoundException;

    CategoryDTO addCategory(Long userId, CategoryDTO categoryDTO, CategoryType type) throws EmsBadRequestException;

    CategoryDTO updateCategory(Long userId, Long categoryId, CategoryDTO categoryDTO) throws EmsBadRequestException;

    void removeCategory(Long userId, Long categoryId, CategoryDeleteDTO categoryDeleteDTO) throws EmsResourceNotFoundException;

    List<SubcategoryDTOext> fetchAllSubcategories(Long userId, Long categoryId);

    SubcategoryDTOext fetchSubcategoryById(Long userId, Long categoryId, Long subcategoryId) throws EmsResourceNotFoundException;

    SubcategoryDTOext addSubcategory(Long userId, Long categoryId, SubcategoryDTO subcategoryDTO) throws EmsBadRequestException;

    SubcategoryDTOext updateSubcategory(Long userId, Long categoryId, Long subcategoryId, SubcategoryDTO subcategoryDTO) throws EmsBadRequestException;

    void removeSubcategory(Long userId, Long categoryId, Long subcategoryId) throws EmsResourceNotFoundException;

    List<CurrencyDTO> fetchAllCurrencies();

    CurrencyDTO fetchCurrencyById(Long currencyId) throws EmsResourceNotFoundException;

    AmountDTO getTotalAmountByDate(Long userId, Integer year, Integer month) throws EmsResourceNotFoundException;

    Double amount(Users user, CategoryType type, LocalDate currentMonth);
}

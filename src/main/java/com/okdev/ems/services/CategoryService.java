package com.okdev.ems.services;

import com.okdev.ems.dto.CategoryDTO;
import com.okdev.ems.dto.SubcategoryDTO;
import com.okdev.ems.dto.SubcategoryDTOext;
import com.okdev.ems.exceptions.EmsBadRequestException;
import com.okdev.ems.exceptions.EmsResourceNotFoundException;
import com.okdev.ems.models.enums.CategoryType;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO> fetchAllCategories(Long userId);

    CategoryDTO fetchCategoryById(Long userId, Long categoryId) throws EmsResourceNotFoundException;

    CategoryDTO addCategory(Long userId, CategoryDTO categoryDTO, CategoryType type) throws EmsBadRequestException;

    CategoryDTO updateCategory(Long userId, Long categoryId, CategoryDTO categoryDTO) throws EmsBadRequestException;

    void removeCategoryWithAllTransactions(Long userId, Long categoryId) throws EmsResourceNotFoundException;

    List<SubcategoryDTOext> fetchAllSubcategories(Long userId, Long categoryId);

    SubcategoryDTOext fetchSubcategoryById(Long userId, Long categoryId, Long subcategoryId) throws EmsResourceNotFoundException;

    SubcategoryDTOext addSubcategory(Long userId, Long categoryId, SubcategoryDTO subcategoryDTO) throws EmsBadRequestException;

    SubcategoryDTOext updateSubcategory(Long userId, Long categoryId, Long subcategoryId, SubcategoryDTO subcategoryDTO) throws EmsBadRequestException;

    void removeSubcategory(Long userId, Long categoryId, Long subcategoryId) throws EmsResourceNotFoundException;
}

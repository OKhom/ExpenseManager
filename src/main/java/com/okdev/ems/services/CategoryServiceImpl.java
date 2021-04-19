package com.okdev.ems.services;

import com.okdev.ems.dto.CategoryDTO;
import com.okdev.ems.dto.SubcategoryDTO;
import com.okdev.ems.dto.SubcategoryDTOext;
import com.okdev.ems.exceptions.EmsBadRequestException;
import com.okdev.ems.exceptions.EmsResourceNotFoundException;
import com.okdev.ems.models.Categories;
import com.okdev.ems.models.Subcategories;
import com.okdev.ems.models.Users;
import com.okdev.ems.models.enums.CategoryType;
import com.okdev.ems.repositories.CategoryRepository;
import com.okdev.ems.repositories.SubcategoryRepository;
import com.okdev.ems.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> fetchAllCategories(Long userId) {
        return categoryRepository.findAll(userId).stream().map(Categories::toDTO).collect(Collectors.toList());
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
            Categories category = Categories.fromDTO(user, categoryDTO, type);
            categoryRepository.save(category);
            return category.toDTO();
        } catch (NullPointerException npe) {
            throw new EmsBadRequestException("Add Category: invalid request");
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
            categoryRepository.save(category);
            return category.toDTO();
        } catch (NullPointerException npe) {
            throw new EmsBadRequestException("Update Category: invalid request");
        }
    }

    @Override
    @Transactional
    public void removeCategoryWithAllTransactions(Long userId, Long categoryId) throws EmsResourceNotFoundException {
        try {
            Categories category = categoryRepository.findById(userId, categoryId);
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
            subcategoryRepository.deleteById(subcategory.getSubcategoryId());
        } catch (NullPointerException npe) {
            throw new EmsResourceNotFoundException("Delete Subcategory: invalid request");
        }
    }
}

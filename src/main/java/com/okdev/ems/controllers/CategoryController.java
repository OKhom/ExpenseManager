package com.okdev.ems.controllers;

import com.okdev.ems.dto.CategoryDTO;
import com.okdev.ems.dto.SubcategoryDTO;
import com.okdev.ems.dto.SubcategoryDTOext;
import com.okdev.ems.models.enums.CategoryType;
import com.okdev.ems.services.CategoryService;
import com.okdev.ems.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<List<CategoryDTO>> getAllCategories(HttpServletRequest request) {
        Long userId = userService.getUserId(request);
        List<CategoryDTO> categories = categoryService.fetchAllCategories(userId);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategoryById(HttpServletRequest request,
                                                       @PathVariable("categoryId") Long categoryId) {
        Long userId = userService.getUserId(request);
        CategoryDTO category = categoryService.fetchCategoryById(userId, categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping("/{categoryType}")
    public ResponseEntity<CategoryDTO> addCategory(HttpServletRequest request,
                                                  @PathVariable("categoryType") Integer categoryType,
                                                  @RequestBody CategoryDTO categoryDTO) {
        Long userId = userService.getUserId(request);
        CategoryDTO category = categoryService.addCategory(userId, categoryDTO, categoryType == 0 ? CategoryType.EXPENSE : CategoryType.INCOME);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(HttpServletRequest request,
                                                      @PathVariable("categoryId") Long categoryId,
                                                      @RequestBody CategoryDTO categoryDTO) {
        Long userId = userService.getUserId(request);
        CategoryDTO categoryUpdated = categoryService.updateCategory(userId, categoryId, categoryDTO);
        return new ResponseEntity<>(categoryUpdated, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Map<String, Boolean>> removeCategory(HttpServletRequest request,
                                                               @PathVariable("categoryId") Long categoryId) {
        Long userId = userService.getUserId(request);
        categoryService.removeCategoryWithAllTransactions(userId, categoryId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}/subcategory")
    ResponseEntity<List<SubcategoryDTOext>> getSubcategories(HttpServletRequest request,
                                                             @PathVariable("categoryId") Long categoryid) {
        Long userId = userService.getUserId(request);
        List<SubcategoryDTOext> subcategories = categoryService.fetchAllSubcategories(userId, categoryid);
        return new ResponseEntity<>(subcategories, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}/subcategory/{subcategoryId}")
    ResponseEntity<SubcategoryDTOext> getSubcategoryById(HttpServletRequest request,
                                                         @PathVariable("categoryId") Long categoryId,
                                                         @PathVariable("subcategoryId") Long subcategoryId) {
        Long userId = userService.getUserId(request);
        SubcategoryDTOext subcategory = categoryService.fetchSubcategoryById(userId, categoryId, subcategoryId);
        return new ResponseEntity<>(subcategory, HttpStatus.OK);
    }

    @PostMapping("/{categoryId}/subcategory")
    public ResponseEntity<SubcategoryDTOext> addSubcategory(HttpServletRequest request,
                                                            @PathVariable("categoryId") Long categoryId,
                                                            @RequestBody SubcategoryDTO subcategoryDTO) {
        Long userId = userService.getUserId(request);
        SubcategoryDTOext subcategory = categoryService.addSubcategory(userId, categoryId, subcategoryDTO);
        return new ResponseEntity<>(subcategory, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}/subcategory/{subcategoryId}")
    ResponseEntity<SubcategoryDTOext> updateSubcategory(HttpServletRequest request,
                                                        @PathVariable("categoryId") Long categoryId,
                                                        @PathVariable("subcategoryId") Long subcategoryId,
                                                        @RequestBody SubcategoryDTO subcategoryDTO) {
        Long userId = userService.getUserId(request);
        SubcategoryDTOext subcategory = categoryService.updateSubcategory(userId, categoryId, subcategoryId, subcategoryDTO);
        return new ResponseEntity<>(subcategory, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}/subcategory/{subcategoryId}")
    ResponseEntity<Map<String, Boolean>> removeSubcategory(HttpServletRequest request,
                                                           @PathVariable("categoryId") Long categoryId,
                                                           @PathVariable("subcategoryId") Long subcategoryId) {
        Long userId = userService.getUserId(request);
        categoryService.removeSubcategory(userId, categoryId, subcategoryId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

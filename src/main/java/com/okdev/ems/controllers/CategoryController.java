package com.okdev.ems.controllers;

import com.okdev.ems.dto.*;
import com.okdev.ems.dto.results.ResultDTO;
import com.okdev.ems.dto.results.SuccessResult;
import com.okdev.ems.models.enums.CategoryType;
import com.okdev.ems.services.CategoryService;
import com.okdev.ems.services.CurrencyService;
import com.okdev.ems.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CurrencyService currencyService;

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

    @GetMapping("/{year}/{month}/type/{categoryType}")
    public ResponseEntity<List<CategoryDTO>> getCategoryByTypeAndDate(HttpServletRequest request,
                                                        @PathVariable("year") Integer year,
                                                        @PathVariable("month") Integer month,
                                                        @PathVariable("categoryType") Integer categoryType) {
        Long userId = userService.getUserId(request);
        List<CategoryDTO> categories = categoryService.fetchCategoriesByTypeAndDate(userId, year, month,
                categoryType == 0 ? CategoryType.EXPENSE : CategoryType.INCOME);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/amount/{year}/{month}")
    public ResponseEntity<AmountDTO> getTotalAmountByDate(HttpServletRequest request,
                                                          @PathVariable("year") Integer year,
                                                          @PathVariable("month") Integer month) {
        Long userId = userService.getUserId(request);
        AmountDTO amount = categoryService.getTotalAmountByDate(userId, year, month);
        return new ResponseEntity<>(amount, HttpStatus.OK);
    }

    @PostMapping("/{categoryType}")
    public ResponseEntity<CategoryDTO> addCategory(HttpServletRequest request,
                                                  @PathVariable("categoryType") Integer categoryType,
                                                  @RequestBody CategoryDTO categoryDTO) {
        Long userId = userService.getUserId(request);
        CategoryDTO category = categoryService.addCategory(userId, categoryDTO,
                categoryType == 0 ? CategoryType.EXPENSE : CategoryType.INCOME);
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
    public ResponseEntity<ResultDTO> removeCategory(HttpServletRequest request,
                                                    @PathVariable("categoryId") Long categoryId,
                                                    @RequestBody CategoryDeleteDTO categoryDeleteDTO) {
        Long userId = userService.getUserId(request);
        categoryService.removeCategory(userId, categoryId, categoryDeleteDTO);
        return new ResponseEntity<>(new SuccessResult(), HttpStatus.OK);
    }

    @GetMapping("/{categoryId}/subcategory")
    ResponseEntity<List<SubcategoryDTOext>> getSubcategories(HttpServletRequest request,
                                                             @PathVariable("categoryId") Long categoryId) {
        Long userId = userService.getUserId(request);
        List<SubcategoryDTOext> subcategories = categoryService.fetchAllSubcategories(userId, categoryId);
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
    ResponseEntity<ResultDTO> removeSubcategory(HttpServletRequest request,
                                                           @PathVariable("categoryId") Long categoryId,
                                                           @PathVariable("subcategoryId") Long subcategoryId) {
        Long userId = userService.getUserId(request);
        categoryService.removeSubcategory(userId, categoryId, subcategoryId);
        return new ResponseEntity<>(new SuccessResult(), HttpStatus.OK);
    }

    @GetMapping("/currency")
    ResponseEntity<List<CurrencyDTO>> fetchAllCurrencies() {
        List<CurrencyDTO> currency = currencyService.fetchAllCurrencies();
        return new ResponseEntity<>(currency, HttpStatus.OK);
    }

    @GetMapping("/currency/{currencyId}")
    ResponseEntity<CurrencyDTO> fetchCurrencyById(@PathVariable("currencyId") Long currencyId) {
        CurrencyDTO currency = currencyService.fetchCurrencyById(currencyId);
        return new ResponseEntity<>(currency, HttpStatus.OK);
    }
}

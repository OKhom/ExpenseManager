package com.okdev.ems.controllers;

import com.okdev.ems.dto.*;
import com.okdev.ems.dto.results.ResultDTO;
import com.okdev.ems.dto.results.SuccessResult;
import com.okdev.ems.models.enums.CategoryType;
import com.okdev.ems.services.CategoryService;
import com.okdev.ems.services.CurrencyService;
import com.okdev.ems.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/category")
@Tag(name = "Categories Controller", description = "Controller for getting, adding, updating and deleting categories and subcategories")
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final CurrencyService currencyService;

    @Autowired
    public CategoryController(UserService userService, CategoryService categoryService, CurrencyService currencyService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.currencyService = currencyService;
    }

    @GetMapping("")
    @Operation(summary = "Get All Categories", description = "Allows to get all categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories(HttpServletRequest request) {
        Long userId = userService.getUserId(request);
        List<CategoryDTO> categories = categoryService.fetchAllCategories(userId);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "Get Category by ID", description = "Allows to get a category by it ID")
    public ResponseEntity<CategoryDTO> getCategoryById(HttpServletRequest request,
                                                       @PathVariable("categoryId") Long categoryId) {
        Long userId = userService.getUserId(request);
        CategoryDTO category = categoryService.fetchCategoryById(userId, categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping("/{year}/{month}/type/{categoryType}")
    @Operation(summary = "Get Categories by Month and Type", description = "Allows to get all categories by month and type")
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
    @Operation(summary = "Get Amount by Month", description = "Allows to get total expense and income by month for all categories")
    public ResponseEntity<AmountDTO> getTotalAmountByDate(HttpServletRequest request,
                                                          @PathVariable("year") Integer year,
                                                          @PathVariable("month") Integer month) {
        Long userId = userService.getUserId(request);
        AmountDTO amount = categoryService.getTotalAmountByDate(userId, year, month);
        return new ResponseEntity<>(amount, HttpStatus.OK);
    }

    @PostMapping("/{categoryType}")
    @Operation(summary = "Add Category by Type", description = "Allows to add a new category by type (0 - expense, 1 - income)")
    public ResponseEntity<CategoryDTO> addCategory(HttpServletRequest request,
                                                  @PathVariable("categoryType") Integer categoryType,
                                                  @RequestBody CategoryDTO categoryDTO) {
        Long userId = userService.getUserId(request);
        CategoryDTO category = categoryService.addCategory(userId, categoryDTO,
                categoryType == 0 ? CategoryType.EXPENSE : CategoryType.INCOME);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "Update Category by ID", description = "Allows to update a category by it ID")
    public ResponseEntity<CategoryDTO> updateCategory(HttpServletRequest request,
                                                      @PathVariable("categoryId") Long categoryId,
                                                      @RequestBody CategoryDTO categoryDTO) {
        Long userId = userService.getUserId(request);
        CategoryDTO categoryUpdated = categoryService.updateCategory(userId, categoryId, categoryDTO);
        return new ResponseEntity<>(categoryUpdated, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "Delete Category by ID", description = "Allows to delete a category by it ID")
    public ResponseEntity<ResultDTO> removeCategory(HttpServletRequest request,
                                                    @PathVariable("categoryId") Long categoryId,
                                                    @RequestBody CategoryDeleteDTO categoryDeleteDTO) {
        Long userId = userService.getUserId(request);
        categoryService.removeCategory(userId, categoryId, categoryDeleteDTO);
        return new ResponseEntity<>(new SuccessResult(), HttpStatus.OK);
    }

    @GetMapping("/{categoryId}/subcategory")
    @Operation(summary = "Get All Subcategories", description = "Allows to get all subcategories by category ID")
    ResponseEntity<List<SubcategoryDTOext>> getSubcategories(HttpServletRequest request,
                                                             @PathVariable("categoryId") Long categoryId) {
        Long userId = userService.getUserId(request);
        List<SubcategoryDTOext> subcategories = categoryService.fetchAllSubcategories(userId, categoryId);
        return new ResponseEntity<>(subcategories, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}/subcategory/{subcategoryId}")
    @Operation(summary = "Get Subcategory by ID", description = "Allows to get a subcategory by it ID and category ID")
    ResponseEntity<SubcategoryDTOext> getSubcategoryById(HttpServletRequest request,
                                                         @PathVariable("categoryId") Long categoryId,
                                                         @PathVariable("subcategoryId") Long subcategoryId) {
        Long userId = userService.getUserId(request);
        SubcategoryDTOext subcategory = categoryService.fetchSubcategoryById(userId, categoryId, subcategoryId);
        return new ResponseEntity<>(subcategory, HttpStatus.OK);
    }

    @PostMapping("/{categoryId}/subcategory")
    @Operation(summary = "Add Subcategory by Category ID", description = "Allows to add a subcategory by category ID")
    public ResponseEntity<SubcategoryDTOext> addSubcategory(HttpServletRequest request,
                                                            @PathVariable("categoryId") Long categoryId,
                                                            @RequestBody SubcategoryDTO subcategoryDTO) {
        Long userId = userService.getUserId(request);
        SubcategoryDTOext subcategory = categoryService.addSubcategory(userId, categoryId, subcategoryDTO);
        return new ResponseEntity<>(subcategory, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}/subcategory/{subcategoryId}")
    @Operation(summary = "Update Subcategory by ID", description = "Allows to update a subcategory by it ID and category ID")
    ResponseEntity<SubcategoryDTOext> updateSubcategory(HttpServletRequest request,
                                                        @PathVariable("categoryId") Long categoryId,
                                                        @PathVariable("subcategoryId") Long subcategoryId,
                                                        @RequestBody SubcategoryDTO subcategoryDTO) {
        Long userId = userService.getUserId(request);
        SubcategoryDTOext subcategory = categoryService.updateSubcategory(userId, categoryId, subcategoryId, subcategoryDTO);
        return new ResponseEntity<>(subcategory, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}/subcategory/{subcategoryId}")
    @Operation(summary = "Delete Subcategory by ID", description = "Allows to delete a subcategory by it ID and category ID")
    ResponseEntity<ResultDTO> removeSubcategory(HttpServletRequest request,
                                                           @PathVariable("categoryId") Long categoryId,
                                                           @PathVariable("subcategoryId") Long subcategoryId) {
        Long userId = userService.getUserId(request);
        categoryService.removeSubcategory(userId, categoryId, subcategoryId);
        return new ResponseEntity<>(new SuccessResult(), HttpStatus.OK);
    }

    @GetMapping("/currency")
    @Operation(summary = "Get All Currencies", description = "Allows to get all currencies")
    ResponseEntity<List<CurrencyDTO>> fetchAllCurrencies() {
        List<CurrencyDTO> currency = currencyService.fetchAllCurrencies();
        return new ResponseEntity<>(currency, HttpStatus.OK);
    }

    @GetMapping("/currency/{currencyId}")
    @Operation(summary = "Get Currency by ID", description = "Allows to get a currency by it ID")
    ResponseEntity<CurrencyDTO> fetchCurrencyById(@PathVariable("currencyId") Long currencyId) {
        CurrencyDTO currency = currencyService.fetchCurrencyById(currencyId);
        return new ResponseEntity<>(currency, HttpStatus.OK);
    }
}

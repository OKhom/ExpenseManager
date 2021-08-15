package com.okdev.ems.controllers;

import com.okdev.ems.dto.BudgetDTO;
import com.okdev.ems.services.BudgetService;
import com.okdev.ems.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/category/{categoryId}/budget")
@Tag(name = "Budgets Controller", description = "Controller for getting, adding, updating and deleting budgets")
@SecurityRequirement(name = "bearerAuth")
public class BudgetController {

    @Autowired
    UserService userService;

    @Autowired
    BudgetService budgetService;

    @GetMapping("")
    @Operation(summary = "Get All Budgets", description = "Allows to get all budgets by category ID")
    public ResponseEntity<List<BudgetDTO>> getAllBudgets(HttpServletRequest request,
                                                         @PathVariable("categoryId") Long categoryId) {
        Long userId = userService.getUserId(request);
        List<BudgetDTO> budgets = budgetService.fetchAllBudgets(userId, categoryId);
        return new ResponseEntity<>(budgets, HttpStatus.OK);
    }

    @GetMapping("/{year}/{month}")
    @Operation(summary = "Get Category's Budget by Month", description = "Allows to get a category's budget by month")
    public ResponseEntity<BudgetDTO> getBudgetByDate(HttpServletRequest request,
                                                     @PathVariable("categoryId") Long categoryId,
                                                     @PathVariable("year") Integer year,
                                                     @PathVariable("month") Integer month) {
        Long userId = userService.getUserId(request);
        BudgetDTO budget = budgetService.fetchBudgetByDate(userId, categoryId, year, month);
        return new ResponseEntity<>(budget, HttpStatus.OK);
    }

    @PostMapping("/{year}/{month}")
    @Operation(summary = "Add Category's Budget by Month", description = "Allows to add a new category's budget by month")
    public ResponseEntity<BudgetDTO> addBudget(HttpServletRequest request,
                                               @PathVariable("categoryId") Long categoryId,
                                               @PathVariable("year") Integer year,
                                               @PathVariable("month") Integer month,
                                               @RequestBody BudgetDTO budgetDTO) {
        Long userId = userService.getUserId(request);
        budgetDTO.setYear(year);
        budgetDTO.setMonth(month);
        BudgetDTO budget = budgetService.addBudget(userId, categoryId, budgetDTO);
        return new ResponseEntity<>(budget, HttpStatus.CREATED);
    }

    @PutMapping("/{year}/{month}")
    @Operation(summary = "Update Category's Budget by Month", description = "Allows to update a category's budget by month")
    public ResponseEntity<BudgetDTO> updateBudgetByDate(HttpServletRequest request,
                                                  @PathVariable("categoryId") Long categoryId,
                                                  @PathVariable("year") Integer year,
                                                  @PathVariable("month") Integer month,
                                                  @RequestBody BudgetDTO budgetDTO) {
        Long userId = userService.getUserId(request);
        BudgetDTO budget = budgetService.updateBudget(userId, categoryId, year, month, budgetDTO);
        return new ResponseEntity<>(budget, HttpStatus.OK);
    }

    @DeleteMapping("/{year}/{month}")
    @Operation(summary = "Delete Category's Budget by Month", description = "Allows to delete a category's budget by month")
    public ResponseEntity<Map<String, Boolean>> removeBudget(HttpServletRequest request,
                                                             @PathVariable("categoryId") Long categoryId,
                                                             @PathVariable("year") Integer year,
                                                             @PathVariable("month") Integer month) {
        Long userId = userService.getUserId(request);
        budgetService.removeBudget(userId, categoryId, year, month);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

package com.okdev.ems.controllers;

import com.okdev.ems.dto.BudgetDTO;
import com.okdev.ems.services.BudgetService;
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
@RequestMapping("/api/category/{categoryId}/budget")
public class BudgetController {

    @Autowired
    UserService userService;

    @Autowired
    BudgetService budgetService;

    @GetMapping("")
    public ResponseEntity<List<BudgetDTO>> getAllBudgets(HttpServletRequest request,
                                                         @PathVariable("categoryId") Long categoryId) {
        Long userId = userService.getUserId(request);
        List<BudgetDTO> budgets = budgetService.fetchAllBudgets(userId, categoryId);
        return new ResponseEntity<>(budgets, HttpStatus.OK);
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<BudgetDTO> getBudgetByDate(HttpServletRequest request,
                                                     @PathVariable("categoryId") Long categoryId,
                                                     @PathVariable("year") Integer year,
                                                     @PathVariable("month") Integer month) {
        Long userId = userService.getUserId(request);
        BudgetDTO budget = budgetService.fetchBudgetByDate(userId, categoryId, year, month);
        return new ResponseEntity<>(budget, HttpStatus.OK);
    }

    @PostMapping("/{year}/{month}")
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

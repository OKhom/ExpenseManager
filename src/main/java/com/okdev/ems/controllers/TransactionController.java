package com.okdev.ems.controllers;

import com.okdev.ems.dto.TransactionDTO;
import com.okdev.ems.dto.results.ResultDTO;
import com.okdev.ems.dto.results.SuccessResult;
import com.okdev.ems.services.TransactionService;
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
@RequestMapping("/api/transaction")
@Tag(name = "Transactions Controller", description = "Controller for getting, adding, updating and deleting transactions")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final UserService userService;
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get All Transactions", description = "Allows to get all transactions by category ID")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions(HttpServletRequest request,
                                                                   @PathVariable("categoryId") Long categoryId) {
        Long userId = userService.getUserId(request);
        List<TransactionDTO> transactions = transactionService.fetchAllTransactions(userId, categoryId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/{year}/{month}")
    @Operation(summary = "Get Category's Transactions by Month", description = "Allows to get category's transactions by month")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByDate(HttpServletRequest request,
                                                                      @PathVariable("year") Integer year,
                                                                      @PathVariable("month") Integer month) {
        Long userId = userService.getUserId(request);
        List<TransactionDTO> transactions = transactionService.fetchTransactionsByDate(userId, year, month);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/{transactionId}/category/{categoryId}")
    @Operation(summary = "Get Category's Transaction by ID", description = "Allows to get a category's transaction by it ID")
    public ResponseEntity<TransactionDTO> getTransactionById(HttpServletRequest request,
                                                             @PathVariable("categoryId") Long categoryId,
                                                             @PathVariable("transactionId") Long transactionId) {
        Long userId = userService.getUserId(request);
        TransactionDTO transaction = transactionService.fetchTransactionById(userId, categoryId, transactionId);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PostMapping("/category/{categoryId}")
    @Operation(summary = "Add Category's Transaction", description = "Allows to add a new category's transaction")
    public ResponseEntity<TransactionDTO> addTransaction(HttpServletRequest request,
                                                         @PathVariable("categoryId") Long categoryId,
                                                         @RequestBody TransactionDTO transactionDTO) {
        Long userId = userService.getUserId(request);
        TransactionDTO transaction = transactionService.addTransaction(userId, categoryId, transactionDTO);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @PostMapping("/category/{categoryId}/subcategory/{subcategoryId}")
    @Operation(summary = "Add Category's Transaction with Subcategory", description = "Allows to add a new category's transaction with subcategory")
    public ResponseEntity<TransactionDTO> addTransactionWithSubcategory(HttpServletRequest request,
                                                         @PathVariable("categoryId") Long categoryId,
                                                         @PathVariable("subcategoryId") Long subcategoryId,
                                                         @RequestBody TransactionDTO transactionDTO) {
        Long userId = userService.getUserId(request);
        TransactionDTO transaction = transactionService.addTransaction(userId, categoryId, subcategoryId, transactionDTO);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @PutMapping("/{transactionId}/category/{categoryId}")
    @Operation(summary = "Update Category's Transaction by ID", description = "Allows to update a category's transaction by it ID")
    public ResponseEntity<TransactionDTO> updateTransaction(HttpServletRequest request,
                                                            @PathVariable("categoryId") Long categoryId,
                                                            @PathVariable("transactionId") Long transactionId,
                                                            @RequestBody TransactionDTO transactionDTO) {
        Long userId = userService.getUserId(request);
        TransactionDTO transaction = transactionService.updateTransaction(userId, categoryId, transactionId, transactionDTO);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PutMapping("/{transactionId}/category/{categoryId}/subcategory/{subcategoryId}")
    @Operation(summary = "Update Category's Transaction by ID with Subcategory", description = "Allows to update a category's transaction by it ID with subcategory")
    public ResponseEntity<TransactionDTO> updateTransaction(HttpServletRequest request,
                                                            @PathVariable("categoryId") Long categoryId,
                                                            @PathVariable("transactionId") Long transactionId,
                                                            @PathVariable("subcategoryId") Long subcategoryId,
                                                            @RequestBody TransactionDTO transactionDTO) {
        Long userId = userService.getUserId(request);
        TransactionDTO transaction = transactionService.updateTransaction(userId, categoryId, transactionId, subcategoryId, transactionDTO);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @DeleteMapping("/{transactionId}/category/{categoryId}")
    @Operation(summary = "Delete Category's Transaction by ID", description = "Allows to delete a category's transaction by it ID")
    public ResponseEntity<ResultDTO> removeTransaction(HttpServletRequest request,
                                                       @PathVariable("categoryId") Long categoryId,
                                                       @PathVariable("transactionId") Long transactionId) {
        Long userId = userService.getUserId(request);
        transactionService.removeTransaction(userId, categoryId, transactionId);
        return new ResponseEntity<>(new SuccessResult(), HttpStatus.OK);
    }
}

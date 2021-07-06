package com.okdev.ems.controllers;

import com.okdev.ems.dto.TransactionDTO;
import com.okdev.ems.services.TransactionService;
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
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    UserService userService;

    @Autowired
    TransactionService transactionService;

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions(HttpServletRequest request,
                                                                   @PathVariable("categoryId") Long categoryId) {
        Long userId = userService.getUserId(request);
        List<TransactionDTO> transactions = transactionService.fetchAllTransactions(userId, categoryId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByDate(HttpServletRequest request,
                                                                      @PathVariable("year") Integer year,
                                                                      @PathVariable("month") Integer month) {
        Long userId = userService.getUserId(request);
        List<TransactionDTO> transactions = transactionService.fetchTransactionsByDate(userId, year, month);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/{transactionId}/category/{categoryId}")
    public ResponseEntity<TransactionDTO> getTransactionById(HttpServletRequest request,
                                                             @PathVariable("categoryId") Long categoryId,
                                                             @PathVariable("transactionId") Long transactionId) {
        Long userId = userService.getUserId(request);
        TransactionDTO transaction = transactionService.fetchTransactionById(userId, categoryId, transactionId);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PostMapping("/category/{categoryId}")
    public ResponseEntity<TransactionDTO> addTransaction(HttpServletRequest request,
                                                         @PathVariable("categoryId") Long categoryId,
                                                         @RequestBody TransactionDTO transactionDTO) {
        Long userId = userService.getUserId(request);
        TransactionDTO transaction = transactionService.addTransaction(userId, categoryId, transactionDTO);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @PostMapping("/category/{categoryId}/subcategory/{subcategoryId}")
    public ResponseEntity<TransactionDTO> addTransactionWithSubcategory(HttpServletRequest request,
                                                         @PathVariable("categoryId") Long categoryId,
                                                         @PathVariable("subcategoryId") Long subcategoryId,
                                                         @RequestBody TransactionDTO transactionDTO) {
        Long userId = userService.getUserId(request);
        TransactionDTO transaction = transactionService.addTransaction(userId, categoryId, subcategoryId, transactionDTO);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @PutMapping("/{transactionId}/category/{categoryId}")
    public ResponseEntity<TransactionDTO> updateTransaction(HttpServletRequest request,
                                                            @PathVariable("categoryId") Long categoryId,
                                                            @PathVariable("transactionId") Long transactionId,
                                                            @RequestBody TransactionDTO transactionDTO) {
        Long userId = userService.getUserId(request);
        TransactionDTO transaction = transactionService.updateTransaction(userId, categoryId, transactionId, transactionDTO);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PutMapping("/{transactionId}/category/{categoryId}/subcategory/{subcategoryId}")
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
    public ResponseEntity<Map<String, Boolean>> removeTransaction(HttpServletRequest request,
                                                                  @PathVariable("categoryId") Long categoryId,
                                                                  @PathVariable("transactionId") Long transactionId) {
        Long userId = userService.getUserId(request);
        transactionService.removeTransaction(userId, categoryId, transactionId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

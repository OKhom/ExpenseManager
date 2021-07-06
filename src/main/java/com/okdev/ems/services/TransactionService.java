package com.okdev.ems.services;

import com.okdev.ems.dto.TransactionDTO;
import com.okdev.ems.exceptions.EmsBadRequestException;
import com.okdev.ems.exceptions.EmsResourceNotFoundException;

import java.util.List;

public interface TransactionService {

    List<TransactionDTO> fetchAllTransactions(Long userId, Long categoryId);

    List<TransactionDTO> fetchTransactionsByDate(Long userId, Integer year, Integer month);

    TransactionDTO fetchTransactionById(Long userId, Long categoryId, Long transactionId) throws EmsResourceNotFoundException;

    TransactionDTO addTransaction(Long userId, Long categoryId, TransactionDTO transactionDTO) throws EmsBadRequestException;

    TransactionDTO addTransaction(Long userId, Long categoryId, Long subcategoryId, TransactionDTO transactionDTO) throws EmsBadRequestException;

    TransactionDTO updateTransaction(Long userId, Long categoryId, Long transactionId, TransactionDTO transactionDTO) throws EmsBadRequestException;

    TransactionDTO updateTransaction(Long userId, Long categoryId, Long transactionId, Long subcategoryId, TransactionDTO transactionDTO) throws EmsBadRequestException;

    void removeTransaction(Long userId, Long categoryId, Long transactionId) throws EmsResourceNotFoundException;
}

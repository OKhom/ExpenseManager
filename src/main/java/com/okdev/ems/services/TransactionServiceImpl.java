package com.okdev.ems.services;

import com.okdev.ems.dto.TransactionDTO;
import com.okdev.ems.exceptions.EmsBadRequestException;
import com.okdev.ems.exceptions.EmsResourceNotFoundException;
import com.okdev.ems.models.Categories;
import com.okdev.ems.models.Subcategories;
import com.okdev.ems.models.Transactions;
import com.okdev.ems.repositories.CategoryRepository;
import com.okdev.ems.repositories.SubcategoryRepository;
import com.okdev.ems.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    SubcategoryRepository subcategoryRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> fetchAllTransactions(Long userId, Long categoryId) {
        return transactionRepository.findAll(userId, categoryId).stream().map(Transactions::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionDTO fetchTransactionById(Long userId, Long categoryId, Long transactionId) throws EmsResourceNotFoundException {
        try {
            return transactionRepository.findById(userId, categoryId, transactionId).toDTO();
        } catch (NullPointerException npe) {
            throw new EmsResourceNotFoundException("Transaction ID not found");
        }
    }

    @Override
    @Transactional
    public TransactionDTO addTransaction(Long userId, Long categoryId, TransactionDTO transactionDTO) throws EmsBadRequestException {
        try {
            Categories category = categoryRepository.findById(userId, categoryId);
            Transactions transaction = Transactions.fromDTO(category, transactionDTO);
            transactionRepository.save(transaction);
            return transaction.toDTO();
        } catch (NullPointerException npe) {
            throw new EmsBadRequestException("Add Transaction: invalid request");
        }
    }

    @Override
    @Transactional
    public TransactionDTO addTransaction(Long userId, Long categoryId, Long subcategoryId, TransactionDTO transactionDTO) throws EmsBadRequestException {
        try {
            Categories category = categoryRepository.findById(userId, categoryId);
            Subcategories subcategory = subcategoryRepository.findById(userId, categoryId, subcategoryId);
            Transactions transaction = Transactions.fromDTO(category, subcategory, transactionDTO);
            transactionRepository.save(transaction);
            return transaction.toDTO();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            throw new EmsBadRequestException("Add Transaction: invalid request");
        }
    }

    @Override
    @Transactional
    public TransactionDTO updateTransaction(Long userId, Long categoryId, Long transactionId, TransactionDTO transactionDTO) throws EmsBadRequestException {
        try {
            Transactions transaction = transactionRepository.findById(userId, categoryId, transactionId);
            if (transactionDTO.getDate() != null)
                transaction.setDate(transactionDTO.getDate());
            if (transactionDTO.getAmount() != null)
                transaction.setAmount(transactionDTO.getAmount());
            if (transactionDTO.getNote() != null)
                transaction.setNote(transactionDTO.getNote());
            transactionRepository.save(transaction);
            return transaction.toDTO();
        } catch (NullPointerException npe) {
            throw new EmsBadRequestException("Update Transaction: invalid request");
        }
    }

    @Override
    @Transactional
    public TransactionDTO updateTransaction(Long userId, Long categoryId, Long transactionId, Long subcategoryId, TransactionDTO transactionDTO) throws EmsBadRequestException {
        try {
            Transactions transaction = transactionRepository.findById(userId, categoryId, transactionId);
            if (subcategoryId > 0) {
                Subcategories subcategory = subcategoryRepository.findById(userId, categoryId, subcategoryId);
                transaction.setSubcategory(subcategory);
            } else
                transaction.setSubcategory(null);
            if (transactionDTO.getAmount() != null)
                transaction.setAmount(transactionDTO.getAmount());
            if (transactionDTO.getDate() != null)
                transaction.setDate(transactionDTO.getDate());
            if (transactionDTO.getNote() != null)
                transaction.setNote(transactionDTO.getNote());
            transactionRepository.save(transaction);
            return transaction.toDTO();
        } catch (NullPointerException npe) {
            throw new EmsBadRequestException("Update Transaction: invalid request");
        }
    }

    @Override
    @Transactional
    public void removeTransaction(Long userId, Long categoryId, Long transactionId) throws EmsResourceNotFoundException {
        try {
            Transactions transaction = transactionRepository.findById(userId, categoryId, transactionId);
            transactionRepository.deleteById(transaction.getTransactionId());
        } catch (NullPointerException npe) {
            throw new EmsResourceNotFoundException("Delete Transaction: invalid request");
        }
    }
}

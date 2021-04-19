package com.okdev.ems.repositories;

import com.okdev.ems.exceptions.EmsResourceNotFoundException;
import com.okdev.ems.models.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long> {

    @Query("SELECT t FROM Users u, Categories c, Transactions t WHERE u.userId = :userId AND c.categoryId = :categoryId")
    List<Transactions> findAll(@Param("userId") Long userId,
                               @Param("categoryId") Long categoryId);

    @Query("SELECT t FROM Users u, Categories c, Transactions t " +
            "WHERE u.userId = :userId AND c.categoryId = :categoryId AND t.transactionId = :transactionId")
    Transactions findById(@Param("userId") Long userId,
                          @Param("categoryId") Long categoryId,
                          @Param("transactionId") Long transactionId) throws EmsResourceNotFoundException;
}

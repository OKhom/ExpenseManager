package com.okdev.ems.repositories;

import com.okdev.ems.exceptions.EmsResourceNotFoundException;
import com.okdev.ems.models.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long> {

    @Query("SELECT t FROM Transactions t WHERE t.category.user.userId = :userId AND t.category.categoryId = :categoryId")
    List<Transactions> findAll(@Param("userId") Long userId,
                               @Param("categoryId") Long categoryId);

    @Query("SELECT t FROM Transactions t WHERE t.category.user.userId = :userId AND t.date >= :from AND t.date < :to ORDER BY t.date DESC")
    List<Transactions> findByDate(@Param("userId") Long userId,
                                  @Param("from") LocalDate from,
                                  @Param("to") LocalDate to);

    @Query("SELECT t FROM Transactions t " +
            "WHERE t.category.user.userId = :userId AND t.category.categoryId = :categoryId AND t.transactionId = :transactionId")
    Transactions findById(@Param("userId") Long userId,
                          @Param("categoryId") Long categoryId,
                          @Param("transactionId") Long transactionId) throws EmsResourceNotFoundException;
}

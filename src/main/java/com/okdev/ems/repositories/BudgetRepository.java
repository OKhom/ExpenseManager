package com.okdev.ems.repositories;

import com.okdev.ems.exceptions.EmsResourceNotFoundException;
import com.okdev.ems.models.Budgets;
import com.okdev.ems.models.embeddedID.BudgetId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budgets, BudgetId> {

    @Query("SELECT b FROM Users u, Categories c, Budgets b " +
            "WHERE u.userId = :userId AND c.categoryId = :categoryId AND b.budgetId.category = :categoryId")
    List<Budgets> findAll(@Param("userId") Long userId,
                          @Param("categoryId") Long categoryId);

    @Query("SELECT b FROM Users u, Categories c, Budgets b " +
            "WHERE u.userId = :userId AND c.categoryId = :categoryId " +
            "AND b.budgetId.category = :categoryId AND b.budgetId.year = :year AND b.budgetId.month = :month")
    Budgets findByDate(@Param("userId") Long userId,
                       @Param("categoryId") Long categoryId,
                       @Param("year") Integer year,
                       @Param("month") Integer month) throws EmsResourceNotFoundException;
}

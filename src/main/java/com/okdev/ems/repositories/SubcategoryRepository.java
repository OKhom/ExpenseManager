package com.okdev.ems.repositories;

import com.okdev.ems.exceptions.EmsResourceNotFoundException;
import com.okdev.ems.models.Subcategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategories, Long> {

    @Query("SELECT s FROM Users u, Categories c, Subcategories s WHERE u.userId = :userId AND c.categoryId = :categoryId")
    List<Subcategories> findAll(@Param("userId") Long userId,
                                @Param("categoryId") Long categoryId);

    @Query("SELECT s FROM Users u, Categories c, Subcategories s " +
            "WHERE u.userId = :userId AND c.categoryId = :categoryId AND s.subcategoryId = :subcategoryId")
    Subcategories findById(@Param("userId") Long userId,
                           @Param("categoryId") Long categoryId,
                           @Param("subcategoryId") Long subcategoryId) throws EmsResourceNotFoundException;
}

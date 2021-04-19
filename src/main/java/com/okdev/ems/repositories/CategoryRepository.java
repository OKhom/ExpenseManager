package com.okdev.ems.repositories;

import com.okdev.ems.exceptions.EmsResourceNotFoundException;
import com.okdev.ems.models.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, Long> {

    @Query("SELECT c FROM Users u, Categories c WHERE u.userId = :userId")
    List<Categories> findAll(@Param("userId") Long userId) throws EmsResourceNotFoundException;

    @Query("SELECT c FROM Users u, Categories c WHERE u.userId = :userId AND c.categoryId = :categoryId")
    Categories findById(@Param("userId") Long userId,
                        @Param("categoryId") Long categoryId) throws EmsResourceNotFoundException;
}

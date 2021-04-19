package com.okdev.ems.repositories;

import com.okdev.ems.exceptions.EmsAuthException;
import com.okdev.ems.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    @Query("SELECT u FROM Users u WHERE u.email = :email")
    Users findUserByEmail(@Param("email") String email) throws EmsAuthException;

    @Query("SELECT u FROM Users u WHERE u.userId = :userId")
    Users findUserById(@Param("userId") Long userId) throws EmsAuthException;

    @Query("SELECT COUNT(u) FROM Users u WHERE u.email = :email")
    Long getCountByEmail(@Param("email") String email) throws EmsAuthException;
}

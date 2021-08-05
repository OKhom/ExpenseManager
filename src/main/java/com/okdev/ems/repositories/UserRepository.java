package com.okdev.ems.repositories;

import com.okdev.ems.exceptions.EmsAuthException;
import com.okdev.ems.models.Users;
import com.okdev.ems.models.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    @Query("SELECT u FROM Users u WHERE u.email = :email")
    Users findUserByEmail(@Param("email") String email) throws EmsAuthException;

    @Query("SELECT u FROM Users u WHERE u.userId = :userId")
    Users findUserById(@Param("userId") Long userId) throws EmsAuthException;

    @Query("SELECT COUNT(u) FROM Users u WHERE u.email = :email")
    Long getCountByEmail(@Param("email") String email) throws EmsAuthException;

    @Query("SELECT COUNT(u) FROM Users u WHERE u.role = :role")
    Long countUsersByRole(@Param("role") UserRole role) throws EmsAuthException;

    @Query("SELECT u FROM Users u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :pattern, '%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :pattern, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :pattern, '%')) " +
            "OR LOWER(u.currency.name) LIKE LOWER(CONCAT('%', :pattern, '%'))" +
            "OR LOWER(u.currency.shortName) LIKE LOWER(CONCAT('%', :pattern, '%'))")
    List<Users> findByPattern(@Param("pattern") String pattern);

//    @Query("SELECT u FROM Users u WHERE u.role = :role")
    List<Users> findUsersByRole(UserRole role, Pageable pageable);
}

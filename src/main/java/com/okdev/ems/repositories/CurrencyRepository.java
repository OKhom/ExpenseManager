package com.okdev.ems.repositories;

import com.okdev.ems.exceptions.EmsResourceNotFoundException;
import com.okdev.ems.models.Currencies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<Currencies, Long> {

    @Query("SELECT COUNT(c) FROM Currencies c")
    Long countAllCurrencies() throws EmsResourceNotFoundException;

    @Query("SELECT c FROM Currencies c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :pattern, '%')) " +
            "OR LOWER(c.shortName) LIKE LOWER(CONCAT('%', :pattern, '%')) " +
            "OR LOWER(c.sign) LIKE LOWER(CONCAT('%', :pattern, '%')) ")
    List<Currencies> findByPattern(@Param("pattern") String pattern);


    Currencies findByShortName(String shortName) throws EmsResourceNotFoundException;
}

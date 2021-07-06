package com.okdev.ems.repositories;

import com.okdev.ems.models.Currencies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currencies, Long> {
}

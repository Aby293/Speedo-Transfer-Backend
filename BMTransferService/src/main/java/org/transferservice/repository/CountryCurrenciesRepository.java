package org.transferservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.transferservice.dto.enums.AccountCurrency;
import org.transferservice.dto.enums.Country;
import org.transferservice.model.CountryCurrency;

import java.util.Optional;

public interface CountryCurrenciesRepository extends JpaRepository<CountryCurrency, Long> {

    Optional<CountryCurrency> findByCurrency(AccountCurrency currency);
    Optional<CountryCurrency> findByCountry(Country country);
}
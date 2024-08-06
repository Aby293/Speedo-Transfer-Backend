package org.transferservice.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.transferservice.model.CountryCurrency;
import org.transferservice.dto.enums.AccountCurrency;
import org.transferservice.dto.enums.Country;
import org.transferservice.repository.CountryCurrenciesRepository;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class CountryCurrenciesService {

    @Autowired
    private CountryCurrenciesRepository repository;

    @PostConstruct
    public void init() {
        List<CountryCurrency> currencies = Arrays.asList(
                CountryCurrency.builder()
                        .currency(AccountCurrency.USD)
                        .flag("\uD83C\uDDFA\uD83C\uDDF8")
                        .country(Country.USA)
                        .currencySymbol("$")
                        .rateToDollar(1.0)
                        .build(),
                CountryCurrency.builder()
                        .currency(AccountCurrency.EUR)
                        .flag("\ud83c\uddea\ud83c\uddfa")
                        .country(Country.EUROPEAN_UNION)
                        .currencySymbol("€")
                        .rateToDollar(1.10)
                        .build(),
                CountryCurrency.builder()
                        .currency(AccountCurrency.JPY)
                        .flag("\uD83C\uDDEF\uD83C\uDDF5")
                        .country(Country.JAPAN)
                        .currencySymbol("¥")
                        .rateToDollar(0.0069)
                        .build(),
                CountryCurrency.builder()
                        .currency(AccountCurrency.GBP)
                        .flag("\uD83C\uDDEC\uD83C\uDDE7")
                        .country(Country.UK)
                        .currencySymbol("£")
                        .rateToDollar(1.28)
                        .build(),
                CountryCurrency.builder()
                        .currency(AccountCurrency.EGP)
                        .flag("\uD83C\uDDEA\uD83C\uDDEC")
                        .country(Country.EGYPT)
                        .currencySymbol("LE")
                        .rateToDollar(0.020)
                        .build(),
                CountryCurrency.builder()
                        .currency(AccountCurrency.SAR)
                        .flag("\ud83c\uddf8\ud83c\udde6")
                        .country(Country.KSA)
                        .currencySymbol("﷼")
                        .rateToDollar(0.027)
                        .build()
        );

        repository.saveAll(currencies);
    }

    public List<CountryCurrency> getCurrencies() {
        return repository.findAll();
    }
}
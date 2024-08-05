package org.transferservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.transferservice.dto.enums.AccountCurrency;
import org.transferservice.dto.enums.Country;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CountryCurrency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private AccountCurrency currency;

    @Column(nullable = false)
    private String flag;

    @Column(nullable = false)
    private Country country;

    @Column
    private String currencySymbol;

    @Column
    private double rateToDollar;
}

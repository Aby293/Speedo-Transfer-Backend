package org.transferservice.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.transferservice.dto.enums.AccountCurrency;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDTO {

    @NotNull
    private String accountNumber;

    @NotNull
    private String accountHolderName;

    private String expirationDate;

    private String cvv;

    private Boolean isDefault;

    private AccountCurrency accountCurrency;

    private double balance;

}

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
public class TransferDTO {
    @NotNull
    private AccountDTO accountDTO;
    @NotNull
    private double sentAmount;
    @NotNull
    private AccountCurrency sendingCurrency;
    @NotNull
    private AccountCurrency receivingCurrency;
}

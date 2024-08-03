package org.transferservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.transferservice.dto.enums.CardCurrency;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferDTO {
    @NotNull
    private CardDTO cardDTO;
    @NotNull
    private double sentAmount;
    @NotNull
    private CardCurrency sendingCurrency;
    @NotNull
    private CardCurrency receivingCurrency;
    @NotNull
    private double receivedAmount;
}

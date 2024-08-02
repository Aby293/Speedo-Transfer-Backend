package org.transferservice.dto;


import jakarta.annotation.Nullable;
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
public class CardDTO {

    @NotNull
    private String cardNumber;

    @NotNull
    private String cardHolderName;

    private String expirationDate;

    private String CVV;

    private Boolean isDefault;

    private CardCurrency cardCurrency;

}

package org.transferservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAccountDTO {

    @NotNull
    private String accountNumber;

    @NotNull
    private String accountHolderName;

    @NotNull
    private String expirationDate;

    @NotNull
    private String cvv;

    private Boolean isDefault;
}

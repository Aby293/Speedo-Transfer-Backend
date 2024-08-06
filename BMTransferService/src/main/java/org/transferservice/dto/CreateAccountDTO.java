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

    private String accountNumber;

    private String accountHolderName;

    private String expirationDate;

    private String cvv;

    @NotNull
    private Boolean isDefault;
}

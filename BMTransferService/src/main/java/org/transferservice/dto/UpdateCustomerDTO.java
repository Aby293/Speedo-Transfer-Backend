package org.transferservice.dto;


import lombok.Data;
import org.transferservice.dto.enums.Country;

import java.time.LocalDate;

@Data
public class UpdateCustomerDTO {

    private String username;

    private String email;

    private String phoneNumber;

    private Country country;

    private LocalDate dateOfBirth;
}

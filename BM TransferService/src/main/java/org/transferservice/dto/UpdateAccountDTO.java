package org.transferservice.dto;


import lombok.Data;
import org.transferservice.dto.enums.Country;
import org.transferservice.dto.enums.Gender;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateAccountDTO {

    private String username;

    private String email;

    private String phoneNumber;

    private Country country;

    private LocalDate dateOfBirth;

    private String password;

}

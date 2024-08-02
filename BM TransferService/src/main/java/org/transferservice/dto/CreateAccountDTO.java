package org.transferservice.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.transferservice.dto.enums.Country;
import org.transferservice.dto.enums.Gender;

import java.time.LocalDate;


@Data
public class CreateAccountDTO {

    private final Long id;

    @NotNull
    private final String username;

    @NotNull
    @Email
    private final String email;

    @NotNull
    private final String phoneNumber;

    @NotNull
    private final Country country;


    @NotNull
    private final Gender gender;

    @NotNull
    private final LocalDate dateOfBirth;

    @NotNull
    @Size(min = 6)
    private final String password;

}

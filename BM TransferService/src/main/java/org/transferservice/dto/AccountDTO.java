package org.transferservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.transferservice.dto.enums.Country;
import org.transferservice.dto.enums.Gender;

import java.time.LocalDate;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDTO {

    private Long id;

    private String username;

    private String email;

    private String phoneNumber;

    private Country country;

    private Gender gender;

    private LocalDate dateOfBirth;

    private List<CardDTO> cards;

}

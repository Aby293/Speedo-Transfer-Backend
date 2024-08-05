package org.transferservice.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.transferservice.dto.CustomerDTO;
import org.transferservice.dto.enums.Country;
import org.transferservice.dto.enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@Entity
@Builder
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Country country;


    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    @Size(min = 8)
    private String password;

    @CreationTimestamp
    private LocalDateTime creationTimeStamp;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Account> accounts;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Account> favoriteRecipients;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Transaction> transactions;



    public CustomerDTO toDTO() {
        return CustomerDTO.builder()
                .id(this.id)
                .username(this.username)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .gender(this.gender)
                .country(this.country)
                .dateOfBirth(this.dateOfBirth)
                .accounts(this.accounts.stream().map(Account::toDTO).toList())
                .build();
    }

}

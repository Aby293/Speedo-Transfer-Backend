package org.transferservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.transferservice.dto.AccountDTO;
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
public class Account {

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
    private String password;

    @CreationTimestamp
    private LocalDateTime creationTimeStamp;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Card> cards;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Card> favoriteRecipients;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Transaction> transactions;



    public AccountDTO toDTO() {
        return AccountDTO.builder()
                .id(this.id)
                .username(this.username)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .gender(this.gender)
                .country(this.country)
                .dateOfBirth(this.dateOfBirth)
                .cards(this.cards.stream().map(Card::toDTO).toList())
                .build();
    }

    public boolean hasCard(String cardNo) {
        return this.cards.stream().anyMatch(card -> card.getCardNumber().equals(cardNo));
    }

    public Card getCard(String cardNo){
        return this.cards.stream().filter(card -> card.getCardNumber().equals(cardNo)).findFirst().get();
    }
}

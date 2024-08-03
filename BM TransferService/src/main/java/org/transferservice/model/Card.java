    package org.transferservice.model;


    import jakarta.persistence.*;
    import jakarta.validation.constraints.Size;
    import lombok.*;
    import org.hibernate.annotations.CreationTimestamp;
    import org.transferservice.dto.CardDTO;
    import org.transferservice.dto.enums.CardCurrency;

    import java.time.LocalDateTime;


    @Data
    @Entity
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class Card {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true)
        @Size(min = 16 , max = 16)
        private String cardNumber;

        @Column(nullable = false)
        private String cardHolderName;

        @Column(nullable = false)
        private String expirationDate;

        @Column(nullable = false)
        private String cvv;

        @Column(nullable = false)
        private boolean isDefault;

        @Column(nullable = false)
        private Double balance;

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        private CardCurrency currency;

        @Column(nullable = false)
        private Boolean active = true;

        @CreationTimestamp
        private LocalDateTime createdAt;

        @ManyToOne
        private Account account;


        public CardDTO toDTO() {
            return CardDTO.builder()
                    .cardNumber(this.cardNumber)
                    .cardHolderName(this.cardHolderName)
                    .expirationDate(this.expirationDate)
                    .cvv(this.cvv)
                    .isDefault(this.isDefault)
                    .cardCurrency(this.currency)
                    .build();
        }

    }

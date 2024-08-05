    package org.transferservice.model;


    import jakarta.persistence.*;
    import jakarta.validation.constraints.Size;
    import lombok.*;
    import org.hibernate.annotations.CreationTimestamp;
    import org.transferservice.dto.AccountDTO;
    import org.transferservice.dto.enums.AccountCurrency;

    import java.time.LocalDateTime;


    @Data
    @Entity
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class Account {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true)
        @Size(min = 16 , max = 16)
        private String accountNumber;

        @Column(nullable = false)
        private String accountHolderName;

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
        private AccountCurrency currency;

        @Column(nullable = false)
        private Boolean active = true;

        @CreationTimestamp
        private LocalDateTime createdAt;

        @ManyToOne
        private Customer customer;


        public AccountDTO toDTO() {
            return AccountDTO.builder()
                    .accountNumber(this.accountNumber)
                    .accountHolderName(this.accountHolderName)
                    .expirationDate(this.expirationDate)
                    .cvv(this.cvv)
                    .balance(balance)
                    .isDefault(this.isDefault)
                    .accountCurrency(this.currency)
                    .build();
        }

    }

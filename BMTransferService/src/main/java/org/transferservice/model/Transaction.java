package org.transferservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.transferservice.dto.TransactionDTO;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime transactionDate;

    @ManyToOne
    private Account senderAccount;

    @ManyToOne
    private Account recipientAccount;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private boolean isSuccessful;

    @ManyToOne
    private Customer senderCustomer;

    @ManyToOne
    private Customer recipientCustomer;

    public TransactionDTO toDTO() {
        return TransactionDTO.builder()
                .senderUser(this.senderCustomer.toDTO())
                .isSuccessful(this.isSuccessful)
                .recipientUser(this.recipientCustomer.toDTO())
                .recipientAccount(this.recipientAccount.toDTO())
                .senderAccount(this.senderAccount.toDTO())
                .transactionDate(this.transactionDate)
                .amount(this.amount)
                .transactionId(this.id)
                .build();
    }

}

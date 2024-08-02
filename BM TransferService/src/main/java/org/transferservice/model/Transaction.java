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
    private UUID transactionId = UUID.randomUUID();

    @CreationTimestamp
    private LocalDateTime transactionDate;

    @ManyToOne
    private Card senderCard;

    @ManyToOne
    private Card recipientCard;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private boolean isSuccessful;

    @ManyToOne
    private Account senderAccount;

    @ManyToOne
    private Account recipientAccount;

    public TransactionDTO toDTO() {
        return TransactionDTO.builder()
                .senderAccount(this.senderAccount)
                .isSuccessful(this.isSuccessful)
                .recipientAccount(this.recipientAccount)
                .recipientCard(this.recipientCard)
                .senderCard(this.senderCard)
                .transactionDate(this.transactionDate)
                .amount(this.amount)
                .transactionId(this.transactionId)
                .build();
    }

}

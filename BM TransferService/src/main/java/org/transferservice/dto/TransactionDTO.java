package org.transferservice.dto;

import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.transferservice.model.Account;
import org.transferservice.model.Card;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDTO {

    private UUID transactionId = UUID.randomUUID();

    private LocalDateTime transactionDate;

    private Card senderCard;

    private Card recipientCard;

    private double amount;

    private boolean isSuccessful;

    private Account senderAccount;

    private Account recipientAccount;
}

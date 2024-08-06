package org.transferservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDTO {

    private Long transactionId ;

    private LocalDateTime transactionDate;

    private AccountDTO senderAccount;

    private AccountDTO recipientAccount;

    private double amount;

    private boolean isSuccessful;

    private CustomerDTO senderUser;

    private CustomerDTO recipientUser;
}

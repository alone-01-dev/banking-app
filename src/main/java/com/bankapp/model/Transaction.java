package com.bankapp.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // DEPOSIT, WITHDRAW, TRANSFER_OUT, TRANSFER_IN
    private Double amount;

    private Double balanceBefore;
    private Double balanceAfter;

    private LocalDateTime dateTime;

    private String status; // SUCCESS, FAILED
    private String reference; // unique transaction ID
    private String description; // optional notes

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    // Optional: receiver info for transfers
    private Long receiverId;
}

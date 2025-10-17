package com.bankapp.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bankapp.model.Transaction;
import com.bankapp.model.User;
import com.bankapp.repository.TransactionRepository;
import com.bankapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    // Deposit
    @Transactional
    public User deposit(Long userId, Double amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Double before = user.getBalance();
        user.setBalance(before + amount);
        userRepository.save(user);

        Transaction tx = Transaction.builder()
                .type("DEPOSIT")
                .amount(amount)
                .balanceBefore(before)
                .balanceAfter(user.getBalance())
                .dateTime(LocalDateTime.now())
                .status("SUCCESS")
                .reference(UUID.randomUUID().toString())
                .user(user)
                .description("Deposit to account")
                .build();

        transactionRepository.save(tx);
        return user;
    }

    // Withdraw
    @Transactional
    public User withdraw(Long userId, Double amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getBalance() < amount)
            throw new RuntimeException("Insufficient funds!");

        Double before = user.getBalance();
        user.setBalance(before - amount);
        userRepository.save(user);

        Transaction tx = Transaction.builder()
                .type("WITHDRAW")
                .amount(amount)
                .balanceBefore(before)
                .balanceAfter(user.getBalance())
                .dateTime(LocalDateTime.now())
                .status("SUCCESS")
                .reference(UUID.randomUUID().toString())
                .user(user)
                .description("Withdraw from account")
                .build();

        transactionRepository.save(tx);
        return user;
    }

    // Transfer
    @Transactional
    public void transfer(Long fromUserId, Long toUserId, Double amount) {
        User fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        if (fromUser.getBalance() < amount)
            throw new RuntimeException("Insufficient funds!");

        Double beforeSender = fromUser.getBalance();
        Double beforeReceiver = toUser.getBalance();

        // Update balances
        fromUser.setBalance(beforeSender - amount);
        toUser.setBalance(beforeReceiver + amount);

        userRepository.save(fromUser);
        userRepository.save(toUser);

        // OUT transaction
        Transaction txOut = Transaction.builder()
                .type("TRANSFER_OUT")
                .amount(amount)
                .balanceBefore(beforeSender)
                .balanceAfter(fromUser.getBalance())
                .dateTime(LocalDateTime.now())
                .status("SUCCESS")
                .reference(UUID.randomUUID().toString())
                .user(fromUser)
                .receiverId(toUser.getId())
                .description("Transfer to user ID " + toUser.getId())
                .build();

        // IN transaction
        Transaction txIn = Transaction.builder()
                .type("TRANSFER_IN")
                .amount(amount)
                .balanceBefore(beforeReceiver)
                .balanceAfter(toUser.getBalance())
                .dateTime(LocalDateTime.now())
                .status("SUCCESS")
                .reference(UUID.randomUUID().toString())
                .user(toUser)
                .receiverId(fromUser.getId())
                .description("Received from user ID " + fromUser.getId())
                .build();

        transactionRepository.save(txOut);
        transactionRepository.save(txIn);
    }

}

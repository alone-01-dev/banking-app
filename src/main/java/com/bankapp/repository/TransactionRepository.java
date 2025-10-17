package com.bankapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankapp.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
	List<Transaction> findByUserId(Long userId);

}

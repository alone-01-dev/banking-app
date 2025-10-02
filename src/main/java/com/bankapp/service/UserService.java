package com.bankapp.service;

import org.springframework.stereotype.Service;

import com.bankapp.model.User;
import com.bankapp.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	public UserService (UserRepository userRepository)
	{
		this.userRepository=userRepository;
	}
	
	public User deposit(Long userId,Double amount)
	{
		User user=userRepository.findById(userId)
				.orElseThrow(() ->new RuntimeException("User not found"));
		user.setBalance(user.getBalance()+amount);
		return userRepository.save(user);
	}
	
	public User withdraw(Long userId,Double amount) {
		User user=userRepository.findById(userId)
				.orElseThrow(() ->new RuntimeException("User not found"));
		if(user.getBalance()<amount)
		{
			throw new RuntimeException("Insufficiant funds!");
		}
		user.setBalance(user.getBalance()-amount);
		return userRepository.save(user);
	}
	
	public void transfer(Long fromUserId,Long toUserId,Double amount)
	{
		User fromUser=userRepository.findById(fromUserId)
				.orElseThrow(() ->new RuntimeException("Sender not found"));
		User toUser=userRepository.findById(toUserId)
				.orElseThrow(() -> new RuntimeException("Reciver not found"));
		if(fromUser.getBalance()<amount)
		{
			throw new RuntimeException("Insufficient funds!");
		}
		fromUser.setBalance(fromUser.getBalance()-amount);
		toUser.setBalance(toUser.getBalance()+amount);
		userRepository.save(fromUser);
		userRepository.save(toUser);
		
	}

}














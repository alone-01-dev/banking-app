package com.bankapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.model.User;
import com.bankapp.repository.UserRepository;
import com.bankapp.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id)
    {
        return userRepository.findById(id).orElse(null);
    }
    
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updateUser) {
        return userRepository.findById(id).map(user -> {
            user.setName(updateUser.getName());
            user.setEmail(updateUser.getEmail());
            user.setPassword(updateUser.getPassword());
            user.setBalance(updateUser.getBalance());
            return userRepository.save(user);
        }).orElse(null);
    }

    
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id)
    {
    	userRepository.deleteById(id);
    	return "User delete with Id" +id;
    }
    
    @PostMapping("/{id}/deposit")
    public User deposit(@PathVariable Long id, @RequestParam Double amount)
    {
    	return userService.deposit(id, amount);
    }
   
    @PostMapping("/{id}/withdraw")
    public User withdraw(@PathVariable Long id,@RequestParam Double amount)
    {
    	return userService.withdraw(id, amount);
    }
    
    @PostMapping("/{fromId}/transfer/{toId}")
    public String transfer(@PathVariable Long fromId,
    		               @PathVariable Long toId,
    		               @RequestParam Double amount)
    {
    	userService.transfer(fromId,toId,amount);
    	return "Transfer successful!";
    }
     

}

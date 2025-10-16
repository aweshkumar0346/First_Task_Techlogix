package com.backend.Meezan.service;

import com.backend.Meezan.model.Account;
import com.backend.Meezan.model.User;
import com.backend.Meezan.repo.AccountRepo;
import com.backend.Meezan.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private AccountRepo accountRepo;

    /** Fetch all users */
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    /** Fetch user by CNIC */
    public Optional<User> getUserByCnic(Long cnic) {
        return repo.findById(cnic);
    }

    /** Create a new user with optional accounts */
    // UserService.java
    public User createUser(User user) {

        if (user.getCnic() == null) {
            throw new IllegalArgumentException("User CNIC must be provided manually.");
        }

        String cnicStr = String.valueOf(user.getCnic());
        if (!cnicStr.matches("\\d{13}")) {
            throw new IllegalArgumentException("CNIC must be exactly 13 digits long.");
        }

        if (repo.existsById(user.getCnic())) {
            throw new IllegalArgumentException("User CNIC already exists.");
        }

        // ✅ Link user to all accounts before saving
        if (user.getAccounts() != null) {
            for (Account account : user.getAccounts()) {
                account.setUser(user); // set user reference
            }
        }

        // Save user; cascade will save all linked accounts
        return repo.save(user);
    }


}

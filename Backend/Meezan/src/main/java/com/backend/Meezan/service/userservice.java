package com.backend.Meezan.service;

import com.backend.Meezan.model.User;
import com.backend.Meezan.repo.usersrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class userservice {

    @Autowired
    private usersrepo repo;

    public List<User> getAllusers(){
        return repo.findAll();
    }


    public Optional<User> getuserById(Long id){
        return repo.findById(id);
    }

    public User createuser(User user){
        return repo.save(user);
    }
}

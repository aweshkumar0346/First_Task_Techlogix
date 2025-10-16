package com.backend.Meezan.repo;

import com.backend.Meezan.model.Account;
import com.backend.Meezan.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {

    /** Find all accounts for a specific user */
    List<Account> findByUser(User user);
}

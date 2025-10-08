package com.backend.Meezan.repo;

import com.backend.Meezan.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface usersrepo extends JpaRepository<User, Long> {

}

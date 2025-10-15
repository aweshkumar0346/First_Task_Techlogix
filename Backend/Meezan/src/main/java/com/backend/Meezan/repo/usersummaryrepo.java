package com.backend.Meezan.repo;

import com.backend.Meezan.model.UserSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface usersummaryrepo extends JpaRepository<UserSummary,Long> {

    @Query(value = "SELECT * FROM user_summary WHERE id = :id", nativeQuery = true)
    UserSummary findByIdValue(Long id);
}

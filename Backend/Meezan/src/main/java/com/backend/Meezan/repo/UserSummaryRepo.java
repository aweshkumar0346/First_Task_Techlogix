package com.backend.Meezan.repo;

import com.backend.Meezan.model.UserSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserSummaryRepo extends JpaRepository<UserSummary,Long> {

    @Query(value = "SELECT * FROM user_summary WHERE cnic = :cnic", nativeQuery = true)
    List<UserSummary> findByCnic(Long cnic);

}

package com.backend.Meezan.repo;

import com.backend.Meezan.model.OtpStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface otpstorerepo extends JpaRepository<OtpStore,Long> {
    Optional<OtpStore> findByEmail(String email);
}

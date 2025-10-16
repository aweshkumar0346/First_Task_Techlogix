package com.backend.Meezan.repo;

import com.backend.Meezan.model.OtpStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OtpStoreRepo extends JpaRepository<OtpStore, Long> {

    Optional<OtpStore> findByEmail(String email);

    // ✅ Stored procedure for saving or updating OTP
    @Procedure(procedureName = "sp_save_or_update_otp")
    void saveOrUpdateOtp(@Param("p_email") String email, @Param("p_otp") String otp);

    // ✅ Function to verify OTP
    @Query(value = "SELECT sp_verify_otp(:email, :otp)", nativeQuery = true)
    Boolean verifyOtp(@Param("email") String email, @Param("otp") String otp);
}

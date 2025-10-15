package com.backend.Meezan.repo;

import com.backend.Meezan.model.OtpStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface otpstorerepo extends JpaRepository<OtpStore,Long> {
    Optional<OtpStore> findByEmail(String email);

    @Procedure(procedureName = "sp_save_or_update_otp")
    void saveOrUpdateOtp(@Param("p_email") String email, @Param("p_otp") String otp);

    // 🔹 New: Call sp_verify_otp function
    @Query(value = "SELECT sp_verify_otp(:email, :otp)", nativeQuery = true)
    Boolean verifyOtp(@Param("email") String email, @Param("otp") String otp);
}

package com.backend.Meezan.repo;

import com.backend.Meezan.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    /** --------------------------
     * Partial update for user fields
     * -------------------------- */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET " +
            "u.registeredContactNumber = :contactNumber, " +
            "u.registeredEmailAddress = :email, " +
            "u.registeredHomeAddress = :homeAddress, " +
            "u.city = :city, " +
            "u.country = :country " +
            "WHERE u.cnic = :cnic")
    int updateUserPartial(Long cnic,
                          String contactNumber,
                          String email,
                          String homeAddress,
                          String city,
                          String country);

    /** --------------------------
     * Create user with CNIC manually (for stored procedure style, optional)
     * -------------------------- */
    // Optional: you can use standard save() from JpaRepository
}

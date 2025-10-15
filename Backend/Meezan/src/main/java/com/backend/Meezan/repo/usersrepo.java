package com.backend.Meezan.repo;

import com.backend.Meezan.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface usersrepo extends JpaRepository<User, Long> {

    @Procedure(procedureName = "sp_create_user")
    void createUser(
            @Param("p_id") Long id,
            @Param("p_name") String name,
            @Param("p_accountTitle") String accountTitle,
            @Param("p_fileStatus") String fileStatus,
            @Param("p_zakatdeductionstatus") String zakatdeductionstatus,
            @Param("p_cnicexpirationdate") java.time.LocalDate cnicexpirationdate,
            @Param("p_lastLoginDetails") java.time.LocalDateTime lastLoginDetails,
            @Param("p_dateOfBirth") java.time.LocalDate dateOfBirth,
            @Param("p_registeredHomeAddress") String registeredHomeAddress,
            @Param("p_registeredContactNumber") String registeredContactNumber,
            @Param("p_registeredEmailAddress") String registeredEmailAddress,
            @Param("p_city") String city,
            @Param("p_country") String country
    );

    @Procedure(procedureName = "sp_update_user")
    void updateUserPartial(
            @Param("p_id") Long id,
            @Param("p_registeredContactNumber") String registeredContactNumber,
            @Param("p_registeredEmailAddress") String registeredEmailAddress,
            @Param("p_registeredHomeAddress") String registeredHomeAddress,
            @Param("p_city") String city,
            @Param("p_country") String country
    );
}

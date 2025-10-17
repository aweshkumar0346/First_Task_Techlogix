package com.backend.Meezan.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name="user_summary")
public class UserSummary {

    @Id
    private Long cnic;
    private String name;
    private String registeredHomeAddress;
    private String registeredContactNumber;
    private String registeredEmailAddress;
    private String accountTitle;
    private String fileStatus;
    private String zakatDeductionStatus;

    public Long getCnic() {
        return cnic;
    }

    public String getName() {
        return name;
    }

    public String getRegisteredHomeAddress() {
        return registeredHomeAddress;
    }

    public String getRegisteredContactNumber() {
        return registeredContactNumber;
    }

    public String getRegisteredEmailAddress() {
        return registeredEmailAddress;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public String getZakatDeductionStatus() {
        return zakatDeductionStatus;
    }
}

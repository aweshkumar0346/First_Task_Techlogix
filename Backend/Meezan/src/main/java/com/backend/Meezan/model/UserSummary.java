package com.backend.Meezan.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name ="user_summary")
public class UserSummary {

    @Id
    private Long id;
    private String name;
    private String accountTitle;
    private String fileStatus;
    private String zakatdeductionstatus;
    private String registeredHomeAddress;
    private String registeredContactNumber;
    private String registeredEmailAddress;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public String getZakatdeductionstatus() {
        return zakatdeductionstatus;
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
}

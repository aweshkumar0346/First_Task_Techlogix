package com.backend.Meezan.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="users")
public class User {

    @Id
    @Column(nullable = false, unique = true)
    private Long cnic;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String registeredHomeAddress;

    @Column(nullable = false)
    private String registeredContactNumber;

    @Column(nullable = false)
    private String registeredEmailAddress;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private LocalDate cnicExpirationDate;

    @Column(nullable = false)
    private LocalDateTime lastLoginDetails;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference // ✅ fix for JSON infinite recursion
    private List<Account> accounts;

    /** ✅ Getters and setters **/

    public Long getCnic() { return cnic; }
    public void setCnic(Long cnic) { this.cnic = cnic; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getRegisteredHomeAddress() { return registeredHomeAddress; }
    public void setRegisteredHomeAddress(String registeredHomeAddress) { this.registeredHomeAddress = registeredHomeAddress; }
    public String getRegisteredContactNumber() { return registeredContactNumber; }
    public void setRegisteredContactNumber(String registeredContactNumber) { this.registeredContactNumber = registeredContactNumber; }
    public String getRegisteredEmailAddress() { return registeredEmailAddress; }
    public void setRegisteredEmailAddress(String registeredEmailAddress) { this.registeredEmailAddress = registeredEmailAddress; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public LocalDate getCnicExpirationDate() { return cnicExpirationDate; }
    public void setCnicExpirationDate(LocalDate cnicExpirationDate) { this.cnicExpirationDate = cnicExpirationDate; }
    public LocalDateTime getLastLoginDetails() { return lastLoginDetails; }
    public void setLastLoginDetails(LocalDateTime lastLoginDetails) { this.lastLoginDetails = lastLoginDetails; }
    public List<Account> getAccounts() { return accounts; }
    public void setAccounts(List<Account> accounts) { this.accounts = accounts; }
}

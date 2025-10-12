package com.backend.Meezan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String accountTitle;

    @Column(nullable = false)
    private String fileStatus;

    @Column(nullable = false)
    private String zakatdeductionstatus;

    @Column(nullable = false)
    private LocalDate cnicexpirationdate;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime lastLoginDetails;

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
    //private String location;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    public String getZakatdeductionstatus() {
        return zakatdeductionstatus;
    }

    public void setZakatdeductionstatus(String zakatdeductionstatus) {
        this.zakatdeductionstatus = zakatdeductionstatus;
    }

    public LocalDate getCnicexpirationdate() {
        return cnicexpirationdate;
    }

    public void setCnicexpirationdate(LocalDate cnicexpirationdate) {
        this.cnicexpirationdate = cnicexpirationdate;
    }

    public LocalDateTime getLastLoginDetails() {
        return lastLoginDetails;
    }

    public void setLastLoginDetails(LocalDateTime lastLoginDetails) {
        this.lastLoginDetails = lastLoginDetails;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRegisteredHomeAddress() {
        return registeredHomeAddress;
    }

    public void setRegisteredHomeAddress(String registeredHomeAddress) {
        this.registeredHomeAddress = registeredHomeAddress;
    }

    public String getRegisteredContactNumber() {
        return registeredContactNumber;
    }

    public void setRegisteredContactNumber(String registeredContactNumber) {
        this.registeredContactNumber = registeredContactNumber;
    }

    public String getRegisteredEmailAddress() {
        return registeredEmailAddress;
    }

    public void setRegisteredEmailAddress(String registeredEmailAddress) {
        this.registeredEmailAddress = registeredEmailAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

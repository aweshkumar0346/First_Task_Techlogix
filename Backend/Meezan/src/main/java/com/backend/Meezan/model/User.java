package com.backend.Meezan.model;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String accountTitle;
    private String jobStatus;
    private String dateCreationStatus;
    private String ercEmployeeStatus;
    private String lastLoginDetails;
    private String dateOfBirth;
    private String registeredHomeAddress;
    private String registeredContactNumber;
    private String registeredEmailAddress;
    private String city;
    private String country;
    //private String location;

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

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getDateCreationStatus() {
        return dateCreationStatus;
    }

    public void setDateCreationStatus(String dateCreationStatus) {
        this.dateCreationStatus = dateCreationStatus;
    }

    public String getErcEmployeeStatus() {
        return ercEmployeeStatus;
    }

    public void setErcEmployeeStatus(String ercEmployeeStatus) {
        this.ercEmployeeStatus = ercEmployeeStatus;
    }

    public String getLastLoginDetails() {
        return lastLoginDetails;
    }

    public void setLastLoginDetails(String lastLoginDetails) {
        this.lastLoginDetails = lastLoginDetails;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
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


}

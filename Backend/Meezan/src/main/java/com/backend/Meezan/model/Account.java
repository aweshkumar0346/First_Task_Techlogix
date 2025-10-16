package com.backend.Meezan.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name="account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @ManyToOne
    @JoinColumn(name = "user_cnic", nullable = false)
    @JsonBackReference // ✅ prevents infinite recursion when serializing User
    private User user;

    @Column(nullable = false)
    private String accountTitle;

    @Column(nullable = false)
    private String fileStatus;

    @Column(nullable = false)
    private String zakatDeductionStatus;

    /** ✅ Getters and setters manually filled **/

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getZakatDeductionStatus() {
        return zakatDeductionStatus;
    }

    public void setZakatDeductionStatus(String zakatDeductionStatus) {
        this.zakatDeductionStatus = zakatDeductionStatus;
    }
}

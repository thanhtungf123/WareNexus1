package com.warenexus.model;

import java.time.LocalDateTime;

public class Account {
    private int accountId;
    private int roleId;
    private String email;
    private String password;
    private boolean isActive;
    private LocalDateTime createdAt;

    public Account() {}

    public Account(int accountId, String email, String password, boolean isActive, LocalDateTime createdAt) {
        this.accountId = accountId;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    
    

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        this.isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "accountId=" + accountId +
                ", email='" + email + '\'' +
                ", active=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}

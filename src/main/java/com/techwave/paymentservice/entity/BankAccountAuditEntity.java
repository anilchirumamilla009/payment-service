package com.techwave.paymentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA entity representing a bank account audit trail record.
 * Captures a snapshot of a bank account's state at a given version.
 */
@Entity
@Table(name = "bank_account_audits")
@EntityListeners(AuditingEntityListener.class)
public class BankAccountAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "resource", nullable = false)
    private UUID resource;

    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "beneficiary")
    private String beneficiary;

    @Column(name = "beneficiary_address", length = 500)
    private String beneficiaryAddress;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "iban", length = 34)
    private String iban;

    @Column(name = "bic", length = 11)
    private String bic;

    @Column(name = "account_number", length = 50)
    private String accountNumber;

    @Column(name = "national_bank_code", length = 50)
    private String nationalBankCode;

    @Column(name = "national_branch_code", length = 50)
    private String nationalBranchCode;

    @Column(name = "national_clearing_code", length = 50)
    private String nationalClearingCode;

    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "country", length = 2)
    private String country;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getResource() {
        return resource;
    }

    public void setResource(UUID resource) {
        this.resource = resource;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getBeneficiaryAddress() {
        return beneficiaryAddress;
    }

    public void setBeneficiaryAddress(String beneficiaryAddress) {
        this.beneficiaryAddress = beneficiaryAddress;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getNationalBankCode() {
        return nationalBankCode;
    }

    public void setNationalBankCode(String nationalBankCode) {
        this.nationalBankCode = nationalBankCode;
    }

    public String getNationalBranchCode() {
        return nationalBranchCode;
    }

    public void setNationalBranchCode(String nationalBranchCode) {
        this.nationalBranchCode = nationalBranchCode;
    }

    public String getNationalClearingCode() {
        return nationalClearingCode;
    }

    public void setNationalClearingCode(String nationalClearingCode) {
        this.nationalClearingCode = nationalClearingCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BankAccountAuditEntity that)) return false;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

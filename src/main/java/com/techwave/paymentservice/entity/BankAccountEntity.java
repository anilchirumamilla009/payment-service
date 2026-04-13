package com.techwave.paymentservice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "bank_accounts", indexes = {
    @Index(name = "idx_bank_account_iban", columnList = "iban"),
    @Index(name = "idx_bank_account_country", columnList = "country")
})
public class BankAccountEntity {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "beneficiary", length = 255)
    private String beneficiary;
    
    @Column(name = "beneficiary_address", columnDefinition = "TEXT")
    private String beneficiaryAddress;
    
    @Column(name = "nickname", length = 255)
    private String nickname;
    
    @Column(name = "iban", length = 34)
    private String iban;
    
    @Column(name = "bic", length = 11)
    private String bic;
    
    @Column(name = "account_number", length = 255)
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
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "version", nullable = false)
    @Version
    private Long version;
    
    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<BankAccountBeneficialOwner> beneficialOwners = new HashSet<>();
    
    public BankAccountEntity() {
    }
    
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
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
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Long getVersion() {
        return version;
    }
    
    public void setVersion(Long version) {
        this.version = version;
    }
    
    public Set<BankAccountBeneficialOwner> getBeneficialOwners() {
        return beneficialOwners;
    }
    
    public void setBeneficialOwners(Set<BankAccountBeneficialOwner> beneficialOwners) {
        this.beneficialOwners = beneficialOwners;
    }
}

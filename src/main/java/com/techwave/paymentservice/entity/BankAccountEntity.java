package com.techwave.paymentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * JPA entity representing a bank account.
 * Supports beneficial owner relationships through a many-to-many
 * join with {@link LegalEntityBase}.
 */
@Entity
@Table(name = "bank_accounts")
@EntityListeners(AuditingEntityListener.class)
public class BankAccountEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

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

    @Version
    @Column(name = "version")
    private Integer version;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "bank_account_beneficial_owners",
        joinColumns = @JoinColumn(name = "bank_account_id"),
        inverseJoinColumns = @JoinColumn(name = "legal_entity_id")
    )
    private Set<LegalEntityBase> beneficialOwners = new HashSet<>();

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public Set<LegalEntityBase> getBeneficialOwners() {
        return beneficialOwners;
    }

    public void setBeneficialOwners(Set<LegalEntityBase> beneficialOwners) {
        this.beneficialOwners = beneficialOwners;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BankAccountEntity that)) return false;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

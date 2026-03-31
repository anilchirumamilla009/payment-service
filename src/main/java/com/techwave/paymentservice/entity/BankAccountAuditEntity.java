package com.techwave.paymentservice.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bank_account_audits")
public class BankAccountAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID resource;

    @Column(nullable = false)
    private Integer version;

    private String beneficiary;
    @Column(name = "beneficiary_address") private String beneficiaryAddress;
    private String nickname;
    private String iban;
    private String bic;
    @Column(name = "account_number") private String accountNumber;
    @Column(name = "national_bank_code") private String nationalBankCode;
    @Column(name = "national_branch_code") private String nationalBranchCode;
    @Column(name = "national_clearing_code") private String nationalClearingCode;
    private String currency;
    private String country;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UUID getResource() { return resource; }
    public void setResource(UUID resource) { this.resource = resource; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    public String getBeneficiary() { return beneficiary; }
    public void setBeneficiary(String beneficiary) { this.beneficiary = beneficiary; }

    public String getBeneficiaryAddress() { return beneficiaryAddress; }
    public void setBeneficiaryAddress(String beneficiaryAddress) { this.beneficiaryAddress = beneficiaryAddress; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getIban() { return iban; }
    public void setIban(String iban) { this.iban = iban; }

    public String getBic() { return bic; }
    public void setBic(String bic) { this.bic = bic; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getNationalBankCode() { return nationalBankCode; }
    public void setNationalBankCode(String nationalBankCode) { this.nationalBankCode = nationalBankCode; }

    public String getNationalBranchCode() { return nationalBranchCode; }
    public void setNationalBranchCode(String nationalBranchCode) { this.nationalBranchCode = nationalBranchCode; }

    public String getNationalClearingCode() { return nationalClearingCode; }
    public void setNationalClearingCode(String nationalClearingCode) { this.nationalClearingCode = nationalClearingCode; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
}


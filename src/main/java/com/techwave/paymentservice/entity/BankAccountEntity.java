package com.techwave.paymentservice.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bank_accounts")
public class BankAccountEntity {

    @Id
    private UUID id;

    @Column(length = 255)
    private String beneficiary;

    @Column(name = "beneficiary_address", length = 500)
    private String beneficiaryAddress;

    @Column(length = 255)
    private String nickname;

    @Column(length = 34)
    private String iban;

    @Column(length = 11)
    private String bic;

    @Column(name = "account_number", length = 50)
    private String accountNumber;

    @Column(name = "national_bank_code", length = 50)
    private String nationalBankCode;

    @Column(name = "national_branch_code", length = 50)
    private String nationalBranchCode;

    @Column(name = "national_clearing_code", length = 50)
    private String nationalClearingCode;

    @Column(length = 3)
    private String currency;

    @Column(length = 2)
    private String country;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

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


package com.techwave.paymentservice.mapper;

import org.springframework.stereotype.Component;

import com.techwave.paymentservice.entity.*;
import com.techwave.paymentservice.model.*;

/**
 * Centralised mapper between JPA entities and API DTOs.
 */
@Component
public class EntityMapper {

    // ── Country ─────────────────────────────────────────────────────────
    public Country toDto(CountryEntity e) {
        Country dto = new Country();
        dto.setId(e.getId());
        dto.setResourceType("countries");
        dto.setName(e.getName());
        dto.setNumericCode(e.getNumericCode());
        dto.setAlpha3Code(e.getAlpha3Code());
        dto.setEurozone(e.getEurozone());
        dto.setSepa(e.getSepa());
        return dto;
    }

    // ── Currency ────────────────────────────────────────────────────────
    public Currency toDto(CurrencyEntity e) {
        Currency dto = new Currency();
        dto.setId(e.getId());
        dto.setResourceType("currencies");
        dto.setName(e.getName());
        return dto;
    }

    // ── Silo ────────────────────────────────────────────────────────────
    public Silo toDto(SiloEntity e) {
        Silo dto = new Silo();
        dto.setId(e.getId());
        dto.setResourceType("silos");
        dto.setName(e.getName());
        dto.setDescription(e.getDescription());
        dto.setEmail(e.getEmail());
        dto.setDefaultBaseCurrency(e.getDefaultBaseCurrency());
        dto.setDefaultCreditLimit(e.getDefaultCreditLimit());
        dto.setDefaultProfitShare(e.getDefaultProfitShare());
        dto.setType(e.getType());
        return dto;
    }

    // ── Corporation ─────────────────────────────────────────────────────
    public Corporation toDto(CorporationEntity e) {
        Corporation dto = new Corporation();
        dto.setId(e.getId());
        dto.setResourceType("corporations");
        dto.setName(e.getName());
        dto.setCode(e.getCode());
        dto.setIncorporationDate(e.getIncorporationDate());
        dto.setIncorporationCountry(e.getIncorporationCountry());
        dto.setType(e.getType());
        dto.setDuplicates(e.getDuplicates());
        return dto;
    }

    public CorporationEntity toEntity(Corporation dto) {
        CorporationEntity e = new CorporationEntity();
        e.setId(dto.getId());
        e.setName(dto.getName());
        e.setCode(dto.getCode());
        e.setIncorporationDate(dto.getIncorporationDate());
        e.setIncorporationCountry(dto.getIncorporationCountry());
        e.setType(dto.getType());
        e.setDuplicates(dto.getDuplicates());
        return e;
    }

    public CorporationAudit toDto(CorporationAuditEntity e) {
        CorporationAudit dto = new CorporationAudit();
        dto.setResource(e.getResource());
        dto.setResourceType("corporation-audits");
        dto.setVersion(e.getVersion());
        dto.setName(e.getName());
        dto.setCode(e.getCode());
        dto.setIncorporationDate(e.getIncorporationDate());
        dto.setIncorporationCountry(e.getIncorporationCountry());
        dto.setType(e.getType());
        dto.setDuplicates(e.getDuplicates());
        return dto;
    }

    public CorporationAuditEntity toAuditEntity(CorporationEntity e, int version) {
        CorporationAuditEntity a = new CorporationAuditEntity();
        a.setResource(e.getId());
        a.setVersion(version);
        a.setName(e.getName());
        a.setCode(e.getCode());
        a.setIncorporationDate(e.getIncorporationDate());
        a.setIncorporationCountry(e.getIncorporationCountry());
        a.setType(e.getType());
        a.setDuplicates(e.getDuplicates());
        return a;
    }

    // ── Person ──────────────────────────────────────────────────────────
    public Person toDto(PersonEntity e) {
        Person dto = new Person();
        dto.setId(e.getId());
        dto.setResourceType("people");
        dto.setFirstName(e.getFirstName());
        dto.setLastName(e.getLastName());
        dto.setDuplicates(e.getDuplicates());
        return dto;
    }

    public PersonEntity toEntity(Person dto) {
        PersonEntity e = new PersonEntity();
        e.setId(dto.getId());
        e.setFirstName(dto.getFirstName());
        e.setLastName(dto.getLastName());
        e.setDuplicates(dto.getDuplicates());
        return e;
    }

    public PersonAudit toDto(PersonAuditEntity e) {
        PersonAudit dto = new PersonAudit();
        dto.setResource(e.getResource());
        dto.setResourceType("person-audits");
        dto.setVersion(e.getVersion());
        dto.setFirstName(e.getFirstName());
        dto.setLastName(e.getLastName());
        dto.setDuplicates(e.getDuplicates());
        return dto;
    }

    public PersonAuditEntity toAuditEntity(PersonEntity e, int version) {
        PersonAuditEntity a = new PersonAuditEntity();
        a.setResource(e.getId());
        a.setVersion(version);
        a.setFirstName(e.getFirstName());
        a.setLastName(e.getLastName());
        a.setDuplicates(e.getDuplicates());
        return a;
    }

    // ── BankAccount ─────────────────────────────────────────────────────
    public BankAccount toDto(BankAccountEntity e) {
        BankAccount dto = new BankAccount();
        dto.setId(e.getId());
        dto.setResourceType("bank-accounts");
        dto.setBeneficiary(e.getBeneficiary());
        dto.setBeneficiaryAddress(e.getBeneficiaryAddress());
        dto.setNickname(e.getNickname());
        dto.setIban(e.getIban());
        dto.setBic(e.getBic());
        dto.setAccountNumber(e.getAccountNumber());
        dto.setNationalBankCode(e.getNationalBankCode());
        dto.setNationalBranchCode(e.getNationalBranchCode());
        dto.setNationalClearingCode(e.getNationalClearingCode());
        dto.setCurrency(e.getCurrency());
        dto.setCountry(e.getCountry());
        return dto;
    }

    public BankAccountEntity toEntity(BankAccount dto) {
        BankAccountEntity e = new BankAccountEntity();
        e.setId(dto.getId());
        e.setBeneficiary(dto.getBeneficiary());
        e.setBeneficiaryAddress(dto.getBeneficiaryAddress());
        e.setNickname(dto.getNickname());
        e.setIban(dto.getIban());
        e.setBic(dto.getBic());
        e.setAccountNumber(dto.getAccountNumber());
        e.setNationalBankCode(dto.getNationalBankCode());
        e.setNationalBranchCode(dto.getNationalBranchCode());
        e.setNationalClearingCode(dto.getNationalClearingCode());
        e.setCurrency(dto.getCurrency());
        e.setCountry(dto.getCountry());
        return e;
    }

    public BankAccountAudit toDto(BankAccountAuditEntity e) {
        BankAccountAudit dto = new BankAccountAudit();
        dto.setResource(e.getResource());
        dto.setResourceType("bank-account-audits");
        dto.setVersion(e.getVersion());
        dto.setBeneficiary(e.getBeneficiary());
        dto.setBeneficiaryAddress(e.getBeneficiaryAddress());
        dto.setNickname(e.getNickname());
        dto.setIban(e.getIban());
        dto.setBic(e.getBic());
        dto.setAccountNumber(e.getAccountNumber());
        dto.setNationalBankCode(e.getNationalBankCode());
        dto.setNationalBranchCode(e.getNationalBranchCode());
        dto.setNationalClearingCode(e.getNationalClearingCode());
        dto.setCurrency(e.getCurrency());
        dto.setCountry(e.getCountry());
        return dto;
    }

    public BankAccountAuditEntity toAuditEntity(BankAccountEntity e, int version) {
        BankAccountAuditEntity a = new BankAccountAuditEntity();
        a.setResource(e.getId());
        a.setVersion(version);
        a.setBeneficiary(e.getBeneficiary());
        a.setBeneficiaryAddress(e.getBeneficiaryAddress());
        a.setNickname(e.getNickname());
        a.setIban(e.getIban());
        a.setBic(e.getBic());
        a.setAccountNumber(e.getAccountNumber());
        a.setNationalBankCode(e.getNationalBankCode());
        a.setNationalBranchCode(e.getNationalBranchCode());
        a.setNationalClearingCode(e.getNationalClearingCode());
        a.setCurrency(e.getCurrency());
        a.setCountry(e.getCountry());
        return a;
    }

    // ── CustomerAccount ─────────────────────────────────────────────────
    public CustomerAccount toDto(CustomerAccountEntity e) {
        CustomerAccount dto = new CustomerAccount();
        dto.setId(e.getId());
        dto.setResourceType("customer-accounts");
        dto.setName(e.getName());
        dto.setDescription(e.getDescription());
        dto.setAccountType(e.getAccountType());
        dto.setAccountState(e.getAccountState());
        dto.setAccountManager(e.getAccountManager());
        dto.setAccountCreationTime(e.getAccountCreationTime());
        dto.setSilo(e.getSilo());
        return dto;
    }

    // ── LegalEntity (beneficial owners) ─────────────────────────────────
    public LegalEntity toLegalEntityDto(BeneficialOwnerEntity bo, PersonEntity person) {
        Person dto = new Person();
        dto.setId(person.getId());
        dto.setResourceType("people");
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setDuplicates(person.getDuplicates());
        return dto;
    }

    public LegalEntity toLegalEntityDto(BeneficialOwnerEntity bo, CorporationEntity corp) {
        Corporation dto = new Corporation();
        dto.setId(corp.getId());
        dto.setResourceType("corporations");
        dto.setName(corp.getName());
        dto.setCode(corp.getCode());
        dto.setIncorporationDate(corp.getIncorporationDate());
        dto.setIncorporationCountry(corp.getIncorporationCountry());
        dto.setType(corp.getType());
        dto.setDuplicates(corp.getDuplicates());
        return dto;
    }
}


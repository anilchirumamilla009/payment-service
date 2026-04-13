package com.techwave.paymentservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;

public class CorporationRequest {
    
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;
    
    @NotBlank(message = "Code is required")
    @Size(min = 1, max = 100, message = "Code must be between 1 and 100 characters")
    private String code;
    
    private LocalDate incorporationDate;
    
    @Pattern(regexp = "^[A-Z]{2}$", message = "Invalid country code (must be 2 uppercase letters)")
    private String incorporationCountry;
    
    @Size(max = 255, message = "Type must not exceed 255 characters")
    private String type;
    
    @Null(message = "Duplicates cannot be set on creation")
    private UUID duplicates;
    
    public CorporationRequest() {
    }
    
    public CorporationRequest(String name, String code) {
        this.name = name;
        this.code = code;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public LocalDate getIncorporationDate() {
        return incorporationDate;
    }
    
    public void setIncorporationDate(LocalDate incorporationDate) {
        this.incorporationDate = incorporationDate;
    }
    
    public String getIncorporationCountry() {
        return incorporationCountry;
    }
    
    public void setIncorporationCountry(String incorporationCountry) {
        this.incorporationCountry = incorporationCountry;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public UUID getDuplicates() {
        return duplicates;
    }
    
    public void setDuplicates(UUID duplicates) {
        this.duplicates = duplicates;
    }
}

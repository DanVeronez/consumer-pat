package br.com.alelo.consumer.consumerpat.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Address {
    
    @NotNull
    @NotBlank
    private String street;
    
    @NotNull
    @NotBlank
    private int number;
    
    @NotNull
    @NotBlank
    private String city;
    
    @NotNull
    @NotBlank
    private String country;
    
    @NotNull
    @NotBlank
    private int portalCode;
}
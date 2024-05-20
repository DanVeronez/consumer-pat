package br.com.alelo.consumer.consumerpat.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Contacts {
        
    @NotNull
    @NotBlank
    private int mobilePhoneNumber;
    
    @NotNull
    @NotBlank
    private int residencePhoneNumber;
    
    @NotNull
    @NotBlank
    private String email;
}

package br.com.alelo.consumer.consumerpat.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Cards {
   
    // Alimentação
    @NotNull
    @NotBlank
    private int foodCardNumber;
    
    @NotNull
    @NotBlank
    private double foodCardBalance;

    // Combustivel
    @NotNull
    @NotBlank
    private int fuelCardNumber;
    
    @NotNull
    @NotBlank
    private double fuelCardBalance;

    // Farmacia
    @NotNull
    @NotBlank
    private int drugstoreNumber;
    
    @NotNull
    @NotBlank
    private double drugstoreCardBalance;
}

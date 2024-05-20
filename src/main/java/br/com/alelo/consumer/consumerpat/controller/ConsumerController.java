package br.com.alelo.consumer.consumerpat.controller;

import br.com.alelo.consumer.consumerpat.entity.Consumer;
import br.com.alelo.consumer.consumerpat.entity.Extract;
import br.com.alelo.consumer.consumerpat.respository.ConsumerRepository;
import br.com.alelo.consumer.consumerpat.respository.ExtractRepository;
import br.com.alelo.consumer.consumerpat.service.ConsumerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Log4j2
@RestController
@RequestMapping("/api/v2/consumers")
public class ConsumerController {

    @Autowired
    ConsumerRepository repository;

    @Autowired
    ExtractRepository extractRepository;

    @Autowired
    private ConsumerService consumerService;

    /* Listar todos os clientes (obs.: tabela possui cerca de 50.000 registros) */
    @GetMapping
    public List<Consumer> getAllConsumers(){
        // return new ResponseEntity<>(consumerService.getAllConsumers(),HttpStatus.OK);
        return consumerService.getAllConsumers();
    }

    @GetMapping("/{id}")
    public Optional<Consumer> getOneConsumer(@PathVariable Integer id) {

        Optional<Consumer> consumer = consumerService.getOneConsumer(id);

        return consumer;
    }
    

    /* Cadastrar novos clientes */
    @PostMapping
    public ResponseEntity<String> saveConsumer(@RequestBody Consumer consumer) {

        consumerService.saveConsumer(consumer);
        
        String messageResponse = "Sucesso ao cadastrar o consumidor [" + consumer.getId() + "]!";

        return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
    }

    // Atualizar cliente, lembrando que não deve ser possível alterar o saldo do cartão
    @PutMapping("/{id}")
    public ResponseEntity<Consumer> updateConsumer(@PathVariable Integer id, @RequestBody Consumer consumer) {

        Consumer consumerReponse = consumerService.updateConsumer(id, consumer);

        return new ResponseEntity<>(consumerReponse, HttpStatus.OK);
    }

    /*
     * Credito de valor no cartão
     *
     * cardNumber: número do cartão
     * value: valor a ser creditado (adicionado ao saldo)
     */
    @RequestMapping(value = "/setcardbalance", method = RequestMethod.GET)
    public void setBalance(int cardNumber, double value) {
        Consumer consumer = null;
        consumer = repository.findByDrugstoreNumber(cardNumber);

        if(consumer != null) {
            // é cartão de farmácia
            consumer.setDrugstoreCardBalance(consumer.getDrugstoreCardBalance() + value);
            repository.save(consumer);
        } else {
            consumer = repository.findByFoodCardNumber(cardNumber);
            if(consumer != null) {
                // é cartão de refeição
                consumer.setFoodCardBalance(consumer.getFoodCardBalance() + value);
                repository.save(consumer);
            } else {
                // É cartão de combustivel
                consumer = repository.findByFuelCardNumber(cardNumber);
                consumer.setFuelCardBalance(consumer.getFuelCardBalance() + value);
                repository.save(consumer);
            }
        }
    }

    /*
     * Débito de valor no cartão (compra)
     *
     * establishmentType: tipo do estabelecimento comercial
     * establishmentName: nome do estabelecimento comercial
     * cardNumber: número do cartão
     * productDescription: descrição do produto
     * value: valor a ser debitado (subtraído)
     */
    @ResponseBody
    @RequestMapping(value = "/buy", method = RequestMethod.GET)
    public void buy(int establishmentType, String establishmentName, int cardNumber, String productDescription, double value) {
        Consumer consumer = null;
        /* O valor só podem ser debitado do catão com o tipo correspondente ao tipo do estabelecimento da compra.

        *  Exemplo: Se a compra é em um estabelecimeto de Alimentação (food) então o valor só pode ser debitado do cartão alimentação
        *
        * Tipos dos estabelcimentos:
        *    1) Alimentação (Food)
        *    2) Farmácia (DrugStore)
        *    3) Posto de combustivel (Fuel)
        */

        if (establishmentType == 1) {
            // Para compras no cartão de alimentação o cliente recebe um desconto de 10%
            Double cashback  = (value / 100) * 10;
            value = value - cashback;

            consumer = repository.findByFoodCardNumber(cardNumber);
            consumer.setFoodCardBalance(consumer.getFoodCardBalance() - value);
            repository.save(consumer);

        }else if(establishmentType == 2) {
            consumer = repository.findByDrugstoreNumber(cardNumber);
            consumer.setDrugstoreCardBalance(consumer.getDrugstoreCardBalance() - value);
            repository.save(consumer);

        } else {
            // Nas compras com o cartão de combustivel existe um acrescimo de 35%;
            Double tax  = (value / 100) * 35;
            value = value + tax;

            consumer = repository.findByFuelCardNumber(cardNumber);
            consumer.setFuelCardBalance(consumer.getFuelCardBalance() - value);
            repository.save(consumer);
        }

        Extract extract = new Extract(establishmentName, productDescription, new Date(), cardNumber, value);
        extractRepository.save(extract);
    }

}

package br.com.alelo.consumer.consumerpat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alelo.consumer.consumerpat.entity.Consumer;
import br.com.alelo.consumer.consumerpat.respository.ConsumerRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ConsumerService {

    @Autowired
    ConsumerRepository consumerRepository;

    public List<Consumer> getAllConsumers() {
        log.info("obtendo todos clientes");
        return consumerRepository.getAllConsumers();
    }
    
}

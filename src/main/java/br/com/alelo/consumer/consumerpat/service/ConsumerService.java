package br.com.alelo.consumer.consumerpat.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import br.com.alelo.consumer.consumerpat.entity.Consumer;
import br.com.alelo.consumer.consumerpat.respository.ConsumerRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ConsumerService {

    @Autowired
    ConsumerRepository consumerRepository;

    /**
     * Método responsável por obter todos os consumidores cadastrados.
     * @return todos os consumidores da base
     */
    public List<Consumer> getAllConsumers() {

        log.info("Consultando todos consumidores...");

        List<Consumer> consumers = consumerRepository.findAll();

        log.info("Sucesso ao consultar todos os consumidores!");

        return consumers; 
    }

    /**
     * Método responsável por cadastrar um novo consumidor
     * @param consumer que será cadastrado
     * @return uma mensagem de sucesso informando qual o ID do consumidor
     */
    public Consumer saveConsumer(Consumer consumer) {

        log.info("Cadastrando consumidor...");

        consumerRepository.save(consumer);

        log.info("Sucesso ao cadastrar o consumidor [{}] com ID [{}]!", consumer.getDocumentNumber(), consumer.getId());
        
        return consumer;
    }

    /**
     * Metodo para atualizar os dados de um consumidor. Exceto o valor em conta.
     * @param consumer a ser atualizado com as alteraçãoes realizadas, sem ID.
     * @return retorna consumidor com os dados atualizados
     */
    public Consumer updateConsumer(Integer id, Consumer consumer){
        
        Optional<Consumer> consumerExists = consumerRepository.findById(id);

        if (consumerExists.isEmpty()) {

            // TODO: Criar Handlers Exceptions
            // Ex. throw new ResourceNotFoundException("Consumidor com ID " + id + " não encontrado!");
            log.error("Consumidor com ID " + id + " nao encontrado!");
            throw new NotFoundException("Consumidor com ID [" + id + "] nao encontrado!");
        }

        log.info("Atualizando dados do consumidor...");

        consumer.setId(id);

        consumerRepository.save(consumer);

        log.info("Sucesso ao atualizar o consumidor [{}] com ID [{}]!", consumer.getDocumentNumber(), consumer.getId());

        return consumer;
    }
    
}

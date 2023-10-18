package com.banco.unifinance.transacoescontasclientes.service;

import com.banco.unifinance.transacoescontasclientes.exception.CustomBadRequestException;
import com.banco.unifinance.transacoescontasclientes.gateway.TransacoesGateway;
import com.banco.unifinance.transacoescontasclientes.model.DepositosSaques;
import com.banco.unifinance.transacoescontasclientes.model.Transferencia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.Serializable;

@Service
public class TransacoesService {

    @Autowired
    private TransacoesGateway gateway;
    private static final Logger logger = LoggerFactory.getLogger(TransacoesService.class);

    @Autowired
    private KafkaTemplate<String, Serializable> kafkaTemplate;

    @Value("${spring.kafka.producer.topic-deposito}")
    private String topicDeposito;

    @Value("${spring.kafka.producer.topic-saque}")
    private String topicSaque;

    @Value("${spring.kafka.producer.topic-transf}")
    private String topicTransferencia;
    public Mono<DepositosSaques> depositarValor(DepositosSaques depositosSaques){
        Mono<Boolean> contaMono = gateway.buscarContaDeposito(depositosSaques.getAgencia(), depositosSaques.getNumero(), depositosSaques.getDigito());
        return contaMono.flatMap(conta -> {
            if (conta.equals(true)){
                kafkaTemplate.send(topicDeposito, depositosSaques);
                return Mono.just(depositosSaques);
            }
            logger.info("Erro na requisição para obter dado da conta.");
            throw new CustomBadRequestException();
        });
    }

    public Mono<DepositosSaques> sacarValor(DepositosSaques depositosSaques){
        Mono<Boolean> contaMono = gateway.buscarContaSaque(depositosSaques.getAgencia(), depositosSaques.getNumero(), depositosSaques.getDigito(), depositosSaques.getValor());
        return contaMono.flatMap(conta -> {
            if (conta.equals(true)){
                kafkaTemplate.send(topicSaque, depositosSaques);
                return Mono.just(depositosSaques);
            }
            logger.info("Erro na requisição para obter dado da conta.");
            throw new CustomBadRequestException();
        });
    }

    public Mono<Transferencia> tranferirValor(Transferencia transferencia){
        if (transferencia.getContaDestino().equals(transferencia.getContaOrigem())){
            logger.info("Conta de origem nao pode ser igual a conta de destino.");
            throw new CustomBadRequestException();
        }
        Mono<Boolean> contaMono = gateway.buscarContaTranferencia(transferencia);
        return contaMono.flatMap(conta -> {
            if (conta.equals(true)){
                kafkaTemplate.send(topicTransferencia, transferencia);
                return Mono.just(transferencia);
            }
            logger.info("Erro na requisição para obter dados das contas.");
            throw new CustomBadRequestException();
        });
    }





}

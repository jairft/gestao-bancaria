package com.banco.unifinance.gestaoclientecontas.service;

import com.banco.unifinance.gestaoclientecontas.entities.Conta;
import com.banco.unifinance.gestaoclientecontas.entities.dto.ContaDTO;
import com.banco.unifinance.gestaoclientecontas.model.DepositosSaques;
import com.banco.unifinance.gestaoclientecontas.model.Transferencia;
import com.banco.unifinance.gestaoclientecontas.repository.ContaRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;


@Component
public class KafkaListenerConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaListenerConsumer.class);
    private final Gson gson = new Gson();

    @Autowired
    private ContaRepository repository;


    @KafkaListener(topics = "gestao-cliente-deposito-topic", groupId = "deposito-group-id")
    public void atualizarSaldoDeposito(@Payload DepositosSaques deposito) {
        logger.info("Deposito realizado para: {}", gson.toJson(deposito));
        Optional<Conta> contaOptional =
                repository.findByAgenciaAndNumeroAndDigito(deposito.getAgencia(), deposito.getNumero(), deposito.getDigito());

        Conta conta = contaOptional.get();

        BigDecimal novoSaldo = conta.getSaldo().add(deposito.getValor());
        conta.setSaldo(novoSaldo);
        repository.save(conta);

        ContaDTO contaDTO = new ContaDTO(conta);

        logger.info("Saldo atualizado para a conta: {}", gson.toJson(contaDTO));

    }

    @KafkaListener(topics = "gestao-cliente-saque-topic", groupId = "saque-group-id")
    public void atualizarSaldoSaque(@Payload DepositosSaques deposito) {
        logger.info("Saque realizado para: {}", gson.toJson(deposito));

        Optional<Conta> contaOptional =
                repository.findByAgenciaAndNumeroAndDigito(deposito.getAgencia(), deposito.getNumero(), deposito.getDigito());

        Conta conta = contaOptional.get();
        BigDecimal valorSaque = deposito.getValor();

        BigDecimal novoSaldo = conta.getSaldo().subtract(valorSaque);
        conta.setSaldo(novoSaldo);
        repository.save(conta);

        ContaDTO contaDTO = new ContaDTO(conta);

        logger.info("Saldo atualizado para a conta: {}", gson.toJson(contaDTO));

    }

    @KafkaListener(topics = "gestao-cliente-transferencia-topic", groupId = "transferencia-group-id")
    public void atualizarSaldoTransferencia(@Payload Transferencia transf) {
        logger.info("Dados da transferência: {}", gson.toJson(transf));

        Optional<Conta> contaOptionalOrigem = repository
                .findByAgenciaAndNumeroAndDigito(transf.getAgenciaOrigem(), transf.getContaOrigem(), transf.getDigitoOrigem());

        Optional<Conta> contaOptionalDestino = repository
                .findByAgenciaAndNumeroAndDigito(transf.getAgenciaDestino(), transf.getContaDestino(), transf.getDigitoDestino());

        if (contaOptionalOrigem.isPresent() && contaOptionalDestino.isPresent()) {
            Conta contaOrigem = contaOptionalOrigem.get();
            Conta contaDestino = contaOptionalDestino.get();
            BigDecimal valorTransferencia = transf.getValor();

            BigDecimal novoSaldoOrigem = contaOrigem.getSaldo().subtract(valorTransferencia);
            BigDecimal novoSaldoDestino = contaDestino.getSaldo().add(valorTransferencia);

            contaOrigem.setSaldo(novoSaldoOrigem);
            contaDestino.setSaldo(novoSaldoDestino);

            repository.save(contaOrigem);
            repository.save(contaDestino);

            ContaDTO contaOrigemDTO = new ContaDTO(contaOrigem);
            ContaDTO contaDestinoDTO = new ContaDTO(contaDestino);

            logger.info("Saldo atualizado para a conta de origem: {}", gson.toJson(contaOrigemDTO));
            logger.info("Saldo atualizado para a conta de destino: {}", gson.toJson(contaDestinoDTO));
        } else {
            logger.error("Contas de origem e/ou destino não encontradas para a transferência.");
        }
    }
}

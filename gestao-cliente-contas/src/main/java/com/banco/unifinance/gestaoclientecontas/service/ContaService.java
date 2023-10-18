package com.banco.unifinance.gestaoclientecontas.service;

import com.banco.unifinance.gestaoclientecontas.entities.Cliente;
import com.banco.unifinance.gestaoclientecontas.entities.Conta;
import com.banco.unifinance.gestaoclientecontas.exception.CustomBadRequestException;
import com.banco.unifinance.gestaoclientecontas.model.Transferencia;
import com.banco.unifinance.gestaoclientecontas.repository.ClienteRepository;
import com.banco.unifinance.gestaoclientecontas.repository.ContaRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ContaService {
    private static final AtomicLong proximoNumeroConta = new AtomicLong(1000);
    private static final Logger logger = LoggerFactory.getLogger(ContaService.class);
    private final Gson gson = new Gson();
    @Autowired
    private ContaRepository contaRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    public Conta criarConta(Conta conta, String cpf) {

        Optional<Cliente> cliente = clienteRepository.findByCpf(cpf);

        try {
            if (Boolean.FALSE.equals(cliente.get().getCpf().isEmpty())){
                conta.setNumero(getNumeroConta());
                conta.setDigito(gerarDigitoAleatorio());
                conta.setSaldo(BigDecimal.ZERO);
                conta.setCliente(cliente.get());

                logger.info("Conta criada: {}", gson.toJson(conta));
                return contaRepository.save(conta);
            }
        }catch (RuntimeException ex){
            logger.error("Cliente não encontrado para o CPF fornecido. {}", ex.getMessage());
            throw new CustomBadRequestException(ex);
        }
        return null;
    }

    public Boolean buscarContaDeposito(String agencia, Long numero, Integer digito) {
        Optional<Conta> contaOptional = contaRepository.findByAgenciaAndNumeroAndDigito(agencia, numero, digito);

        if (!contaOptional.isPresent()) {
            logger.error("Conta não encontrada para depósito: {} - {} - {}", agencia, numero, digito);
            throw new CustomBadRequestException("Conta não encontrada para depósito");
        }

        Conta conta = contaOptional.get();

        if (agencia.equals(conta.getAgencia()) && numero.equals(conta.getNumero()) && digito.equals(conta.getDigito())) {
            return true;
        } else {
            logger.error("Dados incorretos para depósito.");
            throw new CustomBadRequestException();
        }
    }

    public Boolean buscarContaSaque(String agencia, Long numero, Integer digito, BigDecimal valor) {
        Optional<Conta> contaOptional = contaRepository.findByAgenciaAndNumeroAndDigito(agencia, numero, digito);

        if (!contaOptional.isPresent()) {
            logger.error("Conta não encontrada para saque: {} - {} - {}", agencia, numero, digito);
            throw new CustomBadRequestException("Conta não encontrada para saque");
        }

        Conta conta = contaOptional.get();
        BigDecimal saldo = conta.getSaldo();

        if (agencia.equals(conta.getAgencia()) &&
                numero.equals(conta.getNumero()) &&
                digito.equals(conta.getDigito()) &&
                saldo.compareTo(valor) >= 0) {
            return true;
        } else {
            logger.error("Saldo insuficiente para saque. Saldo atual: {}", saldo);
            throw new CustomBadRequestException("Saldo insuficiente para saque");
        }
    }

    public Boolean buscarContasTransferencia(Transferencia transf) {
        logger.info("Iniciando busca de contas para transferência");

        Optional<Conta> contaOptionalOrigem = contaRepository
                .findByAgenciaAndNumeroAndDigito(transf.getAgenciaOrigem(), transf.getContaOrigem(), transf.getDigitoOrigem());

        Optional<Conta> contaOptionalDestino = contaRepository
                .findByAgenciaAndNumeroAndDigito(transf.getAgenciaDestino(), transf.getContaDestino(), transf.getDigitoDestino());

        if (verificarConta(contaOptionalOrigem, contaOptionalDestino, transf)) {
            logger.info("Transferecia realizada..");
            return true;
        } else {
            logger.info("As contas não atendem aos critérios de transferência.");
            return false;
        }
    }

    private boolean verificarConta(Optional<Conta> contaOptionalOrigem, Optional<Conta> contaOptionalDestino, Transferencia transf) {
        logger.info("Verificando contas de origem e destino");

        if (contaOptionalOrigem.isPresent() && contaOptionalDestino.isPresent()) {
            Conta contaOrigem = contaOptionalOrigem.get();
            Conta contaDestino = contaOptionalDestino.get();

            logger.info("Saldo da conta origem é suficiente para transferencia: {}", contaOrigem.getSaldo().compareTo(transf.getValor()) >= 0);

            return agenciaNumeroDigitoIguais(contaOrigem, transf.getAgenciaOrigem(), transf.getContaOrigem(), transf.getDigitoOrigem()) &&
                    agenciaNumeroDigitoIguais(contaDestino, transf.getAgenciaDestino(), transf.getContaDestino(), transf.getDigitoDestino()) &&
                    contaOrigem.getSaldo().compareTo(transf.getValor()) >= 0;
        }

        logger.info("Contas de origem e/ou destino não encontradas.");
        return false;
    }

    private boolean agenciaNumeroDigitoIguais(Conta conta, String agencia, Long numero, Integer digito) {
        boolean iguais = agencia.equals(conta.getAgencia()) &&
                numero.equals(conta.getNumero()) &&
                digito.equals(conta.getDigito());

        return iguais;
    }

    private int gerarDigitoAleatorio() {
        Random random = new Random();
        return random.nextInt(9) + 1;
    }

    public Long getNumeroConta() {
        Long numeroConta;
        return numeroConta = proximoNumeroConta.getAndIncrement();
    }

}

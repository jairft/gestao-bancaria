package com.banco.unifinance.gestaoclientecontas.entities.dto;

import com.banco.unifinance.gestaoclientecontas.entities.Conta;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ContaDTO {
    private Long id;
    private Long numero;
    private Integer digito;
    private String agencia;
    private String tipoConta;
    private BigDecimal saldo;

    public ContaDTO(Conta conta) {
        this.id = conta.getId();
        this.numero = conta.getNumero();
        this.digito = conta.getDigito();
        this.agencia = conta.getAgencia();
        this.tipoConta = conta.getTipoConta().toString(); // Converte o enum para uma String
        this.saldo = conta.getSaldo();
    }

}
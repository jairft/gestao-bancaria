package com.banco.unifinance.transacoescontasclientes.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
@Setter
@Getter
public class DepositosSaques implements Serializable {

    private Long numero;
    private Integer digito;
    private String agencia;
    private BigDecimal valor;

}

package com.banco.unifinance.transacoescontasclientes.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
@Getter
@Setter
public class Transferencia implements Serializable {

    private Long contaOrigem;
    private Long contaDestino;
    private Integer digitoOrigem;
    private Integer digitoDestino;
    private String agenciaOrigem;
    private String agenciaDestino;
    private BigDecimal valor;

}

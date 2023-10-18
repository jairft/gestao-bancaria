package com.banco.unifinance.gestaoclientecontas.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@RequiredArgsConstructor
public class DepositosSaques implements Serializable {

    private Long numero;
    private Integer digito;
    private String agencia;
    private BigDecimal valor;

}

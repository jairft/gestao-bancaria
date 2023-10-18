package com.banco.unifinance.gestaoclientecontas.entities;

import com.banco.unifinance.gestaoclientecontas.entities.enums.TipoConta;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table
@Getter
@Setter
@ToString
public class Conta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long numero;
    private Integer digito;
    private String agencia;
    @Enumerated(EnumType.STRING)
    private TipoConta tipoConta;
    private BigDecimal saldo;

    @OneToOne
    @JoinColumn(name = "cliente_id")
    @JsonIgnore
    private Cliente cliente;

    public Conta() {

    }

}

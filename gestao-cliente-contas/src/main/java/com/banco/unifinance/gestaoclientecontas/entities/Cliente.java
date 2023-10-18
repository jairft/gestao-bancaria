package com.banco.unifinance.gestaoclientecontas.entities;

import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Entity
@Table
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String sobrenome;
    private String cpf;
    private String telefone;
    private String email;
    private String username;
    private String password;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "cliente")
    private Conta conta;
}

package com.banco.unifinance.transacoescontasclientes.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Cliente {


    private Long id;
    private String nome;
    private String sobrenome;
    private String cpf;
    private String telefone;
    private String email;
}

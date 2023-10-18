package com.banco.unifinance.gestaoclientecontas.entities.dto;

import com.banco.unifinance.gestaoclientecontas.entities.Cliente;
import lombok.Getter;

@Getter
public class ClienteDTO {
    private Long id;
    private String nome;
    private String sobrenome;
    private String cpf;
    private String telefone;
    private String email;

    public ClienteDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.sobrenome = cliente.getSobrenome();
        this.cpf = cliente.getCpf();
        this.telefone = cliente.getTelefone();
        this.email = cliente.getEmail();
    }

}
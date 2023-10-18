package com.banco.unifinance.gestaoclientecontas.controller;

import com.banco.unifinance.gestaoclientecontas.entities.Conta;
import com.banco.unifinance.gestaoclientecontas.model.Transferencia;
import com.banco.unifinance.gestaoclientecontas.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/contas")
public class ContaController {

    @Autowired
    private ContaService contaBancariaService;


    @PostMapping("/{cpf}")
    public ResponseEntity<Conta> criarConta(@RequestBody Conta contaBancaria, @PathVariable String cpf) {
        Conta novaConta = contaBancariaService.criarConta(contaBancaria, cpf);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
    }

    @GetMapping("/deposito/{agencia}/{numero}/{digito}")
    public Boolean buscarContaDeposito(@PathVariable String agencia,
                                       @PathVariable Long numero,
                                       @PathVariable Integer digito){
        return contaBancariaService.buscarContaDeposito(agencia, numero, digito);

    }
    @GetMapping("/saque/{agencia}/{numero}/{digito}/{valor}")
    public Boolean buscarContaSaque(@PathVariable String agencia,
                                    @PathVariable Long numero,
                                    @PathVariable Integer digito,
                                    @PathVariable BigDecimal valor){
        return contaBancariaService.buscarContaSaque(agencia, numero, digito, valor);

    }
    @PostMapping("/transferencia")
    public Boolean buscarContaTransferencia(@RequestBody Transferencia transferencia){
        return contaBancariaService.buscarContasTransferencia(transferencia);
    }

}
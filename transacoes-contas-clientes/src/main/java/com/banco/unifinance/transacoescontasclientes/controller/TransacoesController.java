package com.banco.unifinance.transacoescontasclientes.controller;

import com.banco.unifinance.transacoescontasclientes.model.DepositosSaques;
import com.banco.unifinance.transacoescontasclientes.model.Transferencia;
import com.banco.unifinance.transacoescontasclientes.service.TransacoesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/transacoes")
public class TransacoesController {

    @Autowired
    private TransacoesService transacoesService;

    @PostMapping("/deposito")
    public ResponseEntity<Mono<DepositosSaques>> depositar(@RequestBody DepositosSaques depositosSaques){
        return ResponseEntity.ok(transacoesService.depositarValor(depositosSaques));
    }

    @PostMapping("/saque")
    public ResponseEntity<Mono<DepositosSaques>> sacar(@RequestBody DepositosSaques depositosSaques){
        return ResponseEntity.ok(transacoesService.sacarValor(depositosSaques));
    }

    @PostMapping("/transferencia")
    public ResponseEntity<Mono<Transferencia>> tranferir(@RequestBody Transferencia transferencia){
        return ResponseEntity.ok(transacoesService.tranferirValor(transferencia));
    }
}

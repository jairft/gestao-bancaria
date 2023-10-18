package com.banco.unifinance.gestaoclientecontas.controller;

import com.banco.unifinance.gestaoclientecontas.entities.Cliente;
import com.banco.unifinance.gestaoclientecontas.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> salvar(@RequestBody Cliente cliente) {
        Optional<Cliente> clienteResponse = Optional.ofNullable(clienteService.cadastrar(cliente));
        return clienteResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id){
        return ResponseEntity.status(200).body(clienteService.findById(id));
    }
}

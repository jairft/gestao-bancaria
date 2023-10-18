package com.banco.unifinance.gestaoclientecontas.service;

import com.banco.unifinance.gestaoclientecontas.entities.Cliente;
import com.banco.unifinance.gestaoclientecontas.exception.CustomBadRequestException;
import com.banco.unifinance.gestaoclientecontas.repository.ClienteRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    private static final Logger logger = LoggerFactory.getLogger(ContaService.class);
    private final Gson gson = new Gson();
    public Cliente cadastrar(Cliente cliente){
        Optional<Cliente> clienteOptional = clienteRepository.findByCpf(cliente.getCpf());
        if (!clienteOptional.isPresent()){
            logger.info("Cadastrando cliente: {}", gson.toJson(cliente));
            return clienteRepository.save(cliente);
        }
        logger.error("Cliente ja cadastrado para o CPF informado.");
        throw new CustomBadRequestException();
    }

    public Cliente findById(Long id){
        return clienteRepository.findById(id).get();
    }



}

package com.banco.unifinance.gestaoclientecontas.repository;

import com.banco.unifinance.gestaoclientecontas.entities.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    Optional<Conta> findByAgenciaAndNumeroAndDigito(String agencia, Long numero, Integer digito);
}

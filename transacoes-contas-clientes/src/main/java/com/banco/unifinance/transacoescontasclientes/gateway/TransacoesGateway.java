package com.banco.unifinance.transacoescontasclientes.gateway;

import com.banco.unifinance.transacoescontasclientes.exception.CustomBadRequestException;
import com.banco.unifinance.transacoescontasclientes.model.Transferencia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Component
public class TransacoesGateway {

    @Autowired
    @Qualifier("webClient")
    private WebClient webClient;

    @Value("${microsvc.gestao.cliente-deposito}")
    private String gestaoClienteDeposito;

    @Value("${microsvc.gestao.cliente-saque}")
    private String gestaoClienteSaque;

    @Value("${microsvc.gestao.cliente-transf}")
    private String gestaoClienteTranferecia;
    private static final Logger logger = LoggerFactory.getLogger(TransacoesGateway.class);

    public Mono<Boolean> buscarContaDeposito(String agencia, Long numero, Integer digito) {
        return getWebClient()
                .get()
                .uri(getGestaoClienteDeposito(), agencia, numero, digito)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(error -> {
                    logger.error("Erro na requisição: {}", error.getMessage());
                    return Mono.error(new CustomBadRequestException());
                });
    }
    public Mono<Boolean> buscarContaSaque(String agencia, Long conta, Integer digito, BigDecimal valor) {
        return getWebClient()
                .get()
                .uri(getGestaoClienteSaque(), agencia, conta, digito, valor)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(error -> {
                    logger.error("Erro na requisição: {}", error.getMessage());
                    return Mono.error(new CustomBadRequestException());
                });
    }
    public Mono<Boolean> buscarContaTranferencia(Transferencia transferencia) {
        return getWebClient()
                .post()
                .uri(getGestaoClienteTranferecia())
                .bodyValue(transferencia)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(error -> {
                    logger.error("Erro na requisição: {}", error.getMessage());
                    return Mono.error(new CustomBadRequestException());
                });
    }

    public WebClient getWebClient() {
        return webClient;
    }

    public String getGestaoClienteDeposito() {
        return gestaoClienteDeposito;
    }

    public String getGestaoClienteSaque() {
        return gestaoClienteSaque;
    }

    public String getGestaoClienteTranferecia() {
        return gestaoClienteTranferecia;
    }
}

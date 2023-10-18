package com.banco.unifinance.transacoescontasclientes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
@ComponentScan(basePackages = "com.banco.unifinance")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

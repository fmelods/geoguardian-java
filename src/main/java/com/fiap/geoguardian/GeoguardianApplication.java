package com.fiap.geoguardian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.fiap.geoguardian")  // Garante que o Spring escaneie todos os pacotes, incluindo o 'security'
public class GeoguardianApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeoguardianApplication.class, args);
    }
}

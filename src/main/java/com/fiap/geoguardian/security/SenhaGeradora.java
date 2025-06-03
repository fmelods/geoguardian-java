package com.fiap.geoguardian.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SenhaGeradora {

    public static void main(String[] args) {
        // Criação do BCryptPasswordEncoder
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Criptografando a senha "123456"
        String hash = encoder.encode("123456");

        // Mostra o hash gerado no console
        System.out.println("Hash gerado: " + hash);
    }
}

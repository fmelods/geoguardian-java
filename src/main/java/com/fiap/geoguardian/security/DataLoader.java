package com.fiap.geoguardian.security;  // Classe permanece no pacote 'security'

import com.fiap.geoguardian.model.Usuario;
import com.fiap.geoguardian.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component  // Anotação que garante que o Spring vai executar essa classe durante a inicialização
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;  // Repositório para interagir com a tabela de usuários

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;  // Encoder para criptografar a senha

    @Override
    public void run(String... args) throws Exception {
        // Verifica se o usuário já existe no banco de dados
        if (usuarioRepository.findByEmail("admin@geoguardian.com").isEmpty()) {
            // Criação de um novo usuário
            Usuario user = new Usuario();
            user.setNome("Admin Sistema");
            user.setEmail("admin@geoguardian.com");

            // Criptografando a senha "123456"
            user.setSenha(passwordEncoder.encode("123456"));

            // Salvando o usuário no banco de dados
            usuarioRepository.save(user);
            System.out.println("Usuário admin@geoguardian.com criado ou já existe.");
        }
    }
}

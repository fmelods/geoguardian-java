package com.fiap.geoguardian.service;

import com.fiap.geoguardian.model.Usuario;
import com.fiap.geoguardian.repository.TipoUsuarioRepository;
import com.fiap.geoguardian.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public abstract class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public Page<Usuario> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }
    
    public Page<Usuario> findByNome(String nome, Pageable pageable) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome, pageable);
    }
    
    public Page<Usuario> findByTipoUsuarioId(Long tipoUsuarioId, Pageable pageable) {
        return usuarioRepository.findByTipoUsuarioId(tipoUsuarioId, pageable);
    }
    
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }
    
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    public Usuario save(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Já existe um usuário com este email");
        }
        
        if (!tipoUsuarioRepository.existsById(usuario.getTipoUsuario().getId())) {
            throw new RuntimeException("Tipo de usuário não encontrado");
        }
        
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }
    
    public Usuario update(Long id, Usuario usuario) {
        Optional<Usuario> existingUsuario = usuarioRepository.findById(id);
        if (existingUsuario.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }
        
        if (usuarioRepository.existsByEmail(usuario.getEmail()) && 
            !existingUsuario.get().getEmail().equals(usuario.getEmail())) {
            throw new RuntimeException("Já existe um usuário com este email");
        }
        
        if (!tipoUsuarioRepository.existsById(usuario.getTipoUsuario().getId())) {
            throw new RuntimeException("Tipo de usuário não encontrado");
        }
        
        usuario.setId(id);
        usuario.setDataCadastro(existingUsuario.get().getDataCadastro());
        
        if (!usuario.getSenha().equals(existingUsuario.get().getSenha())) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
        
        return usuarioRepository.save(usuario);
    }
    
    public void deleteById(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    public abstract UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}

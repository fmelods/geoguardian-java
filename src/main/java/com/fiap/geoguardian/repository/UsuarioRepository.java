package com.fiap.geoguardian.repository;

import com.fiap.geoguardian.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByEmail(String email);
    
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Page<Usuario> findByNomeContainingIgnoreCase(@Param("nome") String nome, Pageable pageable);
    
    Page<Usuario> findByTipoUsuarioId(Long tipoUsuarioId, Pageable pageable);
    
    boolean existsByEmail(String email);
}

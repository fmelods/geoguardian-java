package com.fiap.geoguardian.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "TB_LOGRADOURO")
public class Logradouro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LOGRADOURO")
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
    @Column(name = "NOME", nullable = false, length = 255)
    private String nome;
    
    @NotNull(message = "Bairro é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_BAIRRO", nullable = false)
    private Bairro bairro;
    
    @OneToMany(mappedBy = "logradouro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AreaRisco> areasRisco;
    
    // Constructors
    public Logradouro() {}
    
    public Logradouro(String nome, Bairro bairro) {
        this.nome = nome;
        this.bairro = bairro;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public Bairro getBairro() {
        return bairro;
    }
    
    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }
    
    public List<AreaRisco> getAreasRisco() {
        return areasRisco;
    }
    
    public void setAreasRisco(List<AreaRisco> areasRisco) {
        this.areasRisco = areasRisco;
    }
}

package com.insurance.customer;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "clienti")
public class Cliente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank private String nome;
    @NotBlank private String cognome;
    @Email @NotBlank private String email;
    private String telefono;
    @Column(name = "numero_polizza")
    @NotBlank private String numeroPolizza;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getNumeroPolizza() { return numeroPolizza; }
    public void setNumeroPolizza(String numeroPolizza) { this.numeroPolizza = numeroPolizza; }
}

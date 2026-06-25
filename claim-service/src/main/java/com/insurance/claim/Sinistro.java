package com.insurance.claim;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Sinistro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clienteId;
    private String tipo;
    private String descrizione;
    private String documentiPresenti;
    private Double importoStimato;

    public Long getId() {
        return id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDocumentiPresenti() {
        return documentiPresenti;
    }

    public void setDocumentiPresenti(String documentiPresenti) {
        this.documentiPresenti = documentiPresenti;
    }

    public Double getImportoStimato() {
        return importoStimato;
    }

    public void setImportoStimato(Double importoStimato) {
        this.importoStimato = importoStimato;
    }
}
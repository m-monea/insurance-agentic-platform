package com.insurance.ai;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AnalisiAi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sinistroId;
    private String cliente;
    private String priorita;
    private String motoreUsato;

    @Column(length = 10000)
    private String reportFinale;

    private LocalDateTime creataIl;

    protected AnalisiAi() {
    }

    public AnalisiAi(Long sinistroId, String cliente, String priorita, String motoreUsato, String reportFinale) {
        this.sinistroId = sinistroId;
        this.cliente = cliente;
        this.priorita = priorita;
        this.motoreUsato = motoreUsato;
        this.reportFinale = reportFinale;
        this.creataIl = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getSinistroId() {
        return sinistroId;
    }

    public String getCliente() {
        return cliente;
    }

    public String getPriorita() {
        return priorita;
    }

    public String getMotoreUsato() {
        return motoreUsato;
    }

    public String getReportFinale() {
        return reportFinale;
    }

    public LocalDateTime getCreataIl() {
        return creataIl;
    }
}
package com.insurance.ai;

public record ClienteDto(
        Long id,
        String nome,
        String cognome,
        String email,
        String telefono,
        String numeroPolizza
) {
}
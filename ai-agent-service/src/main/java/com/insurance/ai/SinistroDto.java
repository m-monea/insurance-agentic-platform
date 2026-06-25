package com.insurance.ai;

import java.math.BigDecimal;

public record SinistroDto(
        Long id,
        Long clienteId,
        String tipo,
        String descrizione,
        String documentiPresenti,
        BigDecimal importoStimato
) {
}
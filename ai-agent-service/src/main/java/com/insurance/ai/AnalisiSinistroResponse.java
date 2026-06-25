package com.insurance.ai;

import java.util.List;

public record AnalisiSinistroResponse(
        Long sinistroId,
        String cliente,
        String riassunto,
        List<String> informazioniMancanti,
        List<String> documentiSuggeriti,
        String priorita,
        String reportFinale,
        String motoreUsato
) {
}
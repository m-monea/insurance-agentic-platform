package com.insurance.claim;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SinistroController.class)
@AutoConfigureMockMvc
class SinistroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SinistroRepository repository;

    @Test
    void trovaTuttiRestituisceSinistri() throws Exception {
        Sinistro sinistro = new Sinistro();
        sinistro.setClienteId(1L);
        sinistro.setTipo("AUTO");
        sinistro.setDescrizione("Tamponamento");
        sinistro.setDocumentiPresenti("foto, denuncia");
        sinistro.setImportoStimato(1200.0);

        when(repository.findAll()).thenReturn(List.of(sinistro));

        mockMvc.perform(get("/api/sinistri"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tipo").value("AUTO"))
                .andExpect(jsonPath("$[0].descrizione").value("Tamponamento"))
                .andExpect(jsonPath("$[0].importoStimato").value(1200.0));
    }

    @Test
    void trovaPerIdRestituisceSinistro() throws Exception {
        Sinistro sinistro = new Sinistro();
        sinistro.setClienteId(1L);
        sinistro.setTipo("CASA");
        sinistro.setDescrizione("Infiltrazione acqua");
        sinistro.setDocumentiPresenti("foto");
        sinistro.setImportoStimato(4200.0);

        when(repository.findById(1L)).thenReturn(Optional.of(sinistro));

        mockMvc.perform(get("/api/sinistri/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("CASA"))
                .andExpect(jsonPath("$.descrizione").value("Infiltrazione acqua"))
                .andExpect(jsonPath("$.importoStimato").value(4200.0));
    }

    @Test
    void trovaPerIdNonEsistenteRestituisce404() throws Exception {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/sinistri/99"))
                .andExpect(status().isNotFound());
    }
}
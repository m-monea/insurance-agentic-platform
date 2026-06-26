package com.insurance.customer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(ClienteController.class)
@AutoConfigureMockMvc
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteRepository repository;

    @Test
    void trovaTuttiRestituisceClienti() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setNome("Mario");
        cliente.setCognome("Rossi");
        cliente.setEmail("mario.rossi@email.it");
        cliente.setTelefono("3331111111");
        cliente.setNumeroPolizza("AUTO-1001");

        when(repository.findAll()).thenReturn(List.of(cliente));

        mockMvc.perform(get("/api/clienti"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome").value("Mario"))
                .andExpect(jsonPath("$[0].cognome").value("Rossi"));
    }

    @Test
    void trovaPerIdRestituisceCliente() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setNome("Laura");
        cliente.setCognome("Bianchi");

        when(repository.findById(1L)).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/api/clienti/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Laura"))
                .andExpect(jsonPath("$.cognome").value("Bianchi"));
    }

    @Test
    void trovaPerIdNonEsistenteRestituisce404() throws Exception {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clienti/99"))
                .andExpect(status().isNotFound());
    }
}
package com.talleros.modules.cliente;

import com.talleros.BaseIntegrationTest;
import com.talleros.modules.cliente.dto.ClienteRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ClienteControllerTest extends BaseIntegrationTest {

    // ── POST /api/clientes ──────────────────────────────────────

    @Test
    void crearCliente_debeRetornar201() throws Exception {
        ClienteRequest request = ClienteRequest.builder()
                .nombre("Carlos")
                .apellido("Méndez")
                .telefono("351-1111111")
                .email("carlos@email.com")
                .build();

        mockMvc.perform(post("/api/clientes")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Carlos"))
                .andExpect(jsonPath("$.apellido").value("Méndez"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void crearCliente_sinNombre_debeRetornar400() throws Exception {
        ClienteRequest request = ClienteRequest.builder()
                .telefono("351-2222222")
                .build();

        mockMvc.perform(post("/api/clientes")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.nombre").exists());
    }

    @Test
    void crearCliente_sinToken_debeRetornar403() throws Exception {
        ClienteRequest request = ClienteRequest.builder()
                .nombre("Carlos")
                .apellido("Méndez")
                .telefono("351-3333333")
                .build();

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isForbidden());
    }

    // ── GET /api/clientes ───────────────────────────────────────

    @Test
    void listarClientes_debeRetornar200() throws Exception {
        mockMvc.perform(get("/api/clientes")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // ── Teléfono duplicado ──────────────────────────────────────

    @Test
    void crearCliente_telefonoDuplicado_debeRetornar409() throws Exception {
        ClienteRequest request = ClienteRequest.builder()
                .nombre("Carlos")
                .apellido("Méndez")
                .telefono("351-4444444")
                .build();

        // Primer cliente
        mockMvc.perform(post("/api/clientes")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isCreated());

        // Segundo cliente con mismo teléfono
        mockMvc.perform(post("/api/clientes")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isConflict());
    }
}
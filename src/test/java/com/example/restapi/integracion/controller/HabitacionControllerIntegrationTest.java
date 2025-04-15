package com.example.restapi.integracion.controller;

import com.example.restapi.model.Habitacion;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;
//import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class HabitacionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllHabitaciones() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/personal/habitaciones")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<Habitacion> habitaciones = Arrays.asList(objectMapper.readValue(content, Habitacion[].class));
        assertNotNull(habitaciones);
        // Verificamos que hay al menos una habitación en la respuesta
        assertFalse(habitaciones.isEmpty());
    }

    @Test
    void getHabitacionesDisponibles() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/personal/habitaciones/disponibles")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        
        String content = result.getResponse().getContentAsString();
        List<Habitacion> habitaciones = Arrays.asList(objectMapper.readValue(content, Habitacion[].class));
        
        // Verificamos que todas las habitaciones devueltas están disponibles
        for (Habitacion habitacion : habitaciones) {
            assertTrue(habitacion.isDisponible());
        }
    }

    @Test
    void getHabitacionesDisponiblesPorTipo() throws Exception {
        // Primero obtenemos todos los tipos de habitaciones disponibles
        MvcResult disponiblesResult = mockMvc.perform(get("/api/personal/habitaciones/disponibles")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        
        List<Habitacion> habitacionesDisponibles = Arrays.asList(
                objectMapper.readValue(disponiblesResult.getResponse().getContentAsString(), Habitacion[].class));
        
        // Si no hay habitaciones disponibles, el test es inválido
        if (habitacionesDisponibles.isEmpty()) {
            return; // Skip this test
        }
        
        // Tomamos el tipo de la primera habitación disponible para probar
        String tipoAProbar = habitacionesDisponibles.get(0).getTipo();
        
        MvcResult result = mockMvc.perform(get("/api/personal/habitaciones/disponibles/tipo/" + tipoAProbar)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        
        List<Habitacion> habitacionesFiltradas = Arrays.asList(
                objectMapper.readValue(result.getResponse().getContentAsString(), Habitacion[].class));
        
        // Verificamos que todas las habitaciones son del tipo solicitado y están disponibles
        for (Habitacion habitacion : habitacionesFiltradas) {
            assertTrue(habitacion.isDisponible());
            assertEquals(tipoAProbar.toLowerCase(), habitacion.getTipo().toLowerCase());
        }
    }

    @Test
    void getHabitacionesDisponiblesPorPrecio() throws Exception {
        // Primero obtenemos todas las habitaciones disponibles
        MvcResult disponiblesResult = mockMvc.perform(get("/api/personal/habitaciones/disponibles")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        
        List<Habitacion> habitacionesDisponibles = Arrays.asList(
                objectMapper.readValue(disponiblesResult.getResponse().getContentAsString(), Habitacion[].class));
        
        // Si no hay habitaciones disponibles, el test es inválido
        if (habitacionesDisponibles.isEmpty()) {
            return; // Skip this test
        }
        
        // Calculamos un precio máximo que incluya algunas pero no todas las habitaciones
        double precioMaximo = 0;
        for (Habitacion h : habitacionesDisponibles) {
            if (h.getPrecioPorNoche() > precioMaximo) {
                precioMaximo = h.getPrecioPorNoche();
            }
        }
        // Usamos un precio un poco menor que el máximo para el test
        precioMaximo = precioMaximo * 0.8;
        
        MvcResult result = mockMvc.perform(get("/api/personal/habitaciones/disponibles/precio/" + precioMaximo)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        
        List<Habitacion> habitacionesFiltradas = Arrays.asList(
                objectMapper.readValue(result.getResponse().getContentAsString(), Habitacion[].class));
        
        // Verificamos que todas las habitaciones están por debajo del precio y disponibles
        for (Habitacion habitacion : habitacionesFiltradas) {
            assertTrue(habitacion.isDisponible());
            assertTrue(habitacion.getPrecioPorNoche() <= precioMaximo);
        }
    }

    @Test
    void getHabitacionUrgente() throws Exception {
        mockMvc.perform(get("/api/personal/habitaciones/urgente")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        // No podemos hacer aserciones detalladas sobre el contenido ya que
        // estamos trabajando con datos existentes que no conocemos
    }
    
    @Test
    void getHabitacionesUrgentes() throws Exception {
        mockMvc.perform(get("/api/personal/habitaciones/urgentes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getHabitacionesNoLimpias() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/personal/habitaciones/noLimpias")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        
        String content = result.getResponse().getContentAsString();
        List<Habitacion> habitacionesNoLimpias = Arrays.asList(objectMapper.readValue(content, Habitacion[].class));
        
        // Verificamos que todas las habitaciones no están limpias
        for (Habitacion habitacion : habitacionesNoLimpias) {
            assertNotEquals("Limpia", habitacion.getEstadoLimpieza());
        }
    }

    @Test
    void getHabitacionParaLimpiezaUrgente() throws Exception {
        mockMvc.perform(get("/api/personal/habitaciones/limpiezaUrgente")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
    /*
    @Test
    void obtenerOcupacionHotel() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/personal/ocupacion")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalHabitaciones").exists())
                .andExpect(jsonPath("$.habitacionesOcupadas").exists())
                .andExpect(jsonPath("$.habitacionesDisponibles").exists())
                .andExpect(jsonPath("$.porcentajeOcupacion").exists())
                .andReturn();
        
        String content = result.getResponse().getContentAsString();
        Map<String, Object> informe = objectMapper.readValue(content, Map.class);
        
        // Verificamos que los campos existen y tienen valores razonables
        assertTrue(informe.containsKey("totalHabitaciones"));
        assertTrue(informe.containsKey("habitacionesOcupadas"));
        assertTrue(informe.containsKey("habitacionesDisponibles"));
        assertTrue(informe.containsKey("porcentajeOcupacion"));
        
        // Verificamos que la matemática es correcta
        int total = Integer.parseInt(informe.get("totalHabitaciones").toString());
        int ocupadas = Integer.parseInt(informe.get("habitacionesOcupadas").toString());
        int disponibles = Integer.parseInt(informe.get("habitacionesDisponibles").toString());
        
        assertEquals(total, ocupadas + disponibles);
    }
        */  
}

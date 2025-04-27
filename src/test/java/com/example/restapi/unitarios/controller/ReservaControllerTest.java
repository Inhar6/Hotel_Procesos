package com.example.restapi.unitarios.controller;

import com.example.restapi.controller.ReservaController;
import com.example.restapi.model.Reserva;
import com.example.restapi.service.ReservaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ReservaControllerTest {

    @Mock
    private ReservaService reservaService;

    @InjectMocks
    private ReservaController reservaController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reservaController).build();
    }

    @Test
    public void testGetAllReservas_Success() throws Exception {
        List<Reserva> reservas = Arrays.asList(new Reserva(), new Reserva());
        when(reservaService.getAllReservas()).thenReturn(reservas);

        mockMvc.perform(get("/api/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(reservaService, times(1)).getAllReservas();
    }

    @Test
    public void testGetReservasPorEmail_Success() throws Exception {
        String email = "test@example.com";
        List<Reserva> reservas = Arrays.asList(new Reserva());
        when(reservaService.getReservasPorEmail(email)).thenReturn(reservas);

        mockMvc.perform(get("/api/reservas/por-email").param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(reservaService, times(1)).getReservasPorEmail(email);
    }

    @Test
    public void testGetReservasPorEmail_NotFound() throws Exception {
        String email = "test@example.com";
        when(reservaService.getReservasPorEmail(email)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/reservas/por-email").param("email", email))
                .andExpect(status().isNotFound());

        verify(reservaService, times(1)).getReservasPorEmail(email);
    }

    @Test
    public void testGetReservaById_Success() throws Exception {
        Long id = 1L;
        Reserva reserva = new Reserva();
        when(reservaService.getReservaById(id)).thenReturn(Optional.of(reserva));

        mockMvc.perform(get("/api/reservas/{id}", id))
                .andExpect(status().isOk());

        verify(reservaService, times(1)).getReservaById(id);
    }

    @Test
    public void testGetReservaById_NotFound() throws Exception {
        Long id = 1L;
        when(reservaService.getReservaById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reservas/{id}", id))
                .andExpect(status().isNotFound());

        verify(reservaService, times(1)).getReservaById(id);
    }

    @Test
    public void testHacerReserva_Success() throws Exception {
        Map<String, Object> datos = new HashMap<>();
        datos.put("clienteId", 1L);
        datos.put("habitacionId", 2L);
        datos.put("fechaCheckIn", "2025-05-01");
        datos.put("fechaCheckOut", "2025-05-05");
        datos.put("metodoPago", "Tarjeta");

        when(reservaService.reservarHabitacion(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString()))
                .thenReturn(true);

        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clienteId\":1,\"habitacionId\":2,\"fechaCheckIn\":\"2025-05-01\",\"fechaCheckOut\":\"2025-05-05\",\"metodoPago\":\"Tarjeta\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reserva realizada con éxito."));

        verify(reservaService, times(1)).reservarHabitacion(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    public void testHacerReserva_Failure() throws Exception {
        Map<String, Object> datos = new HashMap<>();
        datos.put("clienteId", 1L);
        datos.put("habitacionId", 2L);
        datos.put("fechaCheckIn", "2025-05-01");
        datos.put("fechaCheckOut", "2025-05-05");
        datos.put("metodoPago", "Tarjeta");

        when(reservaService.reservarHabitacion(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString()))
                .thenReturn(false);

        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clienteId\":1,\"habitacionId\":2,\"fechaCheckIn\":\"2025-05-01\",\"fechaCheckOut\":\"2025-05-05\",\"metodoPago\":\"Tarjeta\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se pudo realizar la reserva."));

        verify(reservaService, times(1)).reservarHabitacion(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    public void testHacerReserva_InvalidData() throws Exception {
        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clienteId\":1,\"habitacionId\":2,\"fechaCheckIn\":\"invalid-date\",\"fechaCheckOut\":\"2025-05-05\",\"metodoPago\":\"Tarjeta\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error en los datos de la reserva."));

        verify(reservaService, never()).reservarHabitacion(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    public void testModificarReserva_Success() throws Exception {
        Long id = 1L;
        Map<String, Object> datos = new HashMap<>();
        datos.put("habitacionId", 2L);
        datos.put("fechaCheckIn", "2025-05-01");
        datos.put("fechaCheckOut", "2025-05-05");
        datos.put("metodoPago", "Tarjeta");

        when(reservaService.modificarReserva(eq(id), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString()))
                .thenReturn(true);

        mockMvc.perform(put("/api/reservas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"habitacionId\":2,\"fechaCheckIn\":\"2025-05-01\",\"fechaCheckOut\":\"2025-05-05\",\"metodoPago\":\"Tarjeta\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reserva modificada con éxito."));

        verify(reservaService, times(1)).modificarReserva(eq(id), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    public void testModificarReserva_MissingFields() throws Exception {
        Long id = 1L;
        mockMvc.perform(put("/api/reservas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"habitacionId\":2}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Faltan campos obligatorios en los datos."));

        verify(reservaService, never()).modificarReserva(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    public void testModificarReserva_InvalidDateFormat() throws Exception {
        Long id = 1L;
        mockMvc.perform(put("/api/reservas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"habitacionId\":2,\"fechaCheckIn\":\"invalid-date\",\"fechaCheckOut\":\"2025-05-05\",\"metodoPago\":\"Tarjeta\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Las fechas deben estar en formato YYYY-MM-DD."));

        verify(reservaService, never()).modificarReserva(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    public void testCancelarReserva_Success() throws Exception {
        Long id = 1L;
        Reserva reserva = new Reserva();
        reserva.setEstado("Activa");
        when(reservaService.getReservaById(id)).thenReturn(Optional.of(reserva));
        when(reservaService.cancelarReserva(id)).thenReturn(true);

        mockMvc.perform(put("/api/reservas/cancelar/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Reserva cancelada correctamente."));

        verify(reservaService, times(1)).getReservaById(id);
        verify(reservaService, times(1)).cancelarReserva(id);
    }

    @Test
    public void testCancelarReserva_AlreadyCancelled() throws Exception {
        Long id = 1L;
        Reserva reserva = new Reserva();
        reserva.setEstado("Cancelada");
        when(reservaService.getReservaById(id)).thenReturn(Optional.of(reserva));

        mockMvc.perform(put("/api/reservas/cancelar/{id}", id))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("La reserva ya está cancelada."));

        verify(reservaService, times(1)).getReservaById(id);
        verify(reservaService, never()).cancelarReserva(id);
    }

    @Test
    public void testCancelarReserva_NotFound() throws Exception {
        Long id = 1L;
        when(reservaService.getReservaById(id)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/reservas/cancelar/{id}", id))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se pudo cancelar la reserva. Verifica que exista."));

        verify(reservaService, times(1)).getReservaById(id);
        verify(reservaService, times(1)).cancelarReserva(id);
    }

    @Test
    public void testObtenerNuevasReservas_Success() throws Exception {
        List<Reserva> reservas = Arrays.asList(new Reserva(), new Reserva());
        when(reservaService.obtenerReservasPendientes()).thenReturn(reservas);

        mockMvc.perform(get("/api/reservas/nuevas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(reservaService, times(1)).obtenerReservasPendientes();
    }
}
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

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
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
    public void testGetAllReservas() throws Exception {
        // Arrange
        List<Reserva> reservas = Arrays.asList(new Reserva(), new Reserva());
        when(reservaService.getAllReservas()).thenReturn(reservas);

        // Act & Assert
        mockMvc.perform(get("/api/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(reservaService, times(1)).getAllReservas();
    }

    @Test
    public void testGetReservasPorEmail_Encontradas() throws Exception {
        // Arrange
        String email = "test@example.com";
        List<Reserva> reservas = Arrays.asList(new Reserva());
        when(reservaService.getReservasPorEmail(email)).thenReturn(reservas);

        // Act & Assert
        mockMvc.perform(get("/api/reservas/por-email").param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(reservaService, times(1)).getReservasPorEmail(email);
    }

    @Test
    public void testGetReservasPorEmail_NoEncontradas() throws Exception {
        // Arrange
        String email = "test@example.com";
        when(reservaService.getReservasPorEmail(email)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/reservas/por-email").param("email", email))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());

        verify(reservaService, times(1)).getReservasPorEmail(email);
    }

    @Test
    public void testGetReservaById_Encontrada() throws Exception {
        // Arrange
        Long id = 1L;
        Reserva reserva = new Reserva();
        when(reservaService.getReservaById(eq(id))).thenReturn(Optional.of(reserva));

        // Act & Assert
        mockMvc.perform(get("/api/reservas/{id}", id))
                .andExpect(status().isOk());

        verify(reservaService, times(1)).getReservaById(eq(id));
    }

    @Test
    public void testGetReservaById_NoEncontrada() throws Exception {
        // Arrange
        Long id = 1L;
        when(reservaService.getReservaById(eq(id))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/reservas/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());

        verify(reservaService, times(1)).getReservaById(eq(id));
    }

    @Test
    public void testHacerReserva_Exito() throws Exception {
        // Arrange
        when(reservaService.reservarHabitacion(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString()))
                .thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clienteId\":1,\"habitacionId\":2,\"fechaCheckIn\":\"2025-05-01\",\"fechaCheckOut\":\"2025-05-05\",\"metodoPago\":\"Tarjeta\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reserva realizada con éxito."));

        verify(reservaService, times(1)).reservarHabitacion(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    public void testHacerReserva_Fallo() throws Exception {
        // Arrange
        when(reservaService.reservarHabitacion(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString()))
                .thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clienteId\":1,\"habitacionId\":2,\"fechaCheckIn\":\"2025-05-01\",\"fechaCheckOut\":\"2025-05-05\",\"metodoPago\":\"Tarjeta\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se pudo realizar la reserva."));

        verify(reservaService, times(1)).reservarHabitacion(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    public void testHacerReserva_FormatoFechaInvalido() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clienteId\":1,\"habitacionId\":2,\"fechaCheckIn\":\"invalid-date\",\"fechaCheckOut\":\"2025-05-05\",\"metodoPago\":\"Tarjeta\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error en los datos de la reserva."));

        verify(reservaService, never()).reservarHabitacion(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    public void testHacerReserva_IdInvalido() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clienteId\":\"invalid\",\"habitacionId\":2,\"fechaCheckIn\":\"2025-05-01\",\"fechaCheckOut\":\"2025-05-05\",\"metodoPago\":\"Tarjeta\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error en los datos de la reserva."));

        verify(reservaService, never()).reservarHabitacion(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    public void testHacerReserva_ErrorInesperado() throws Exception {
        // Arrange
        when(reservaService.reservarHabitacion(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString()))
                .thenThrow(new RuntimeException("Error inesperado"));

        // Act & Assert
        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clienteId\":1,\"habitacionId\":2,\"fechaCheckIn\":\"2025-05-01\",\"fechaCheckOut\":\"2025-05-05\",\"metodoPago\":\"Tarjeta\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error en los datos de la reserva."));

        verify(reservaService, times(1)).reservarHabitacion(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    public void testModificarReserva_Exito() throws Exception {
        // Arrange
        Long id = 1L;
        when(reservaService.modificarReserva(eq(id), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString()))
                .thenReturn(true);

        // Act & Assert
        mockMvc.perform(put("/api/reservas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"habitacionId\":2,\"fechaCheckIn\":\"2025-05-01\",\"fechaCheckOut\":\"2025-05-05\",\"metodoPago\":\"Tarjeta\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reserva modificada con éxito."));

        verify(reservaService, times(1)).modificarReserva(eq(id), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    public void testModificarReserva_Fallo() throws Exception {
        // Arrange
        Long id = 1L;
        when(reservaService.modificarReserva(eq(id), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString()))
                .thenReturn(false);

        // Act & Assert
        mockMvc.perform(put("/api/reservas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"habitacionId\":2,\"fechaCheckIn\":\"2025-05-01\",\"fechaCheckOut\":\"2025-05-05\",\"metodoPago\":\"Tarjeta\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se pudo modificar la reserva. Verifica que la reserva exista y la habitación esté disponible."));

        verify(reservaService, times(1)).modificarReserva(eq(id), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    public void testModificarReserva_FaltaHabitacionId() throws Exception {
        // Arrange
        Long id = 1L;

        // Act & Assert
        mockMvc.perform(put("/api/reservas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fechaCheckIn\":\"2025-05-01\",\"fechaCheckOut\":\"2025-05-05\",\"metodoPago\":\"Tarjeta\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Faltan campos obligatorios en los datos."));

        verify(reservaService, never()).modificarReserva(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    public void testModificarReserva_FaltaFechaCheckIn() throws Exception {
        // Arrange
        Long id = 1L;

        // Act & Assert
        mockMvc.perform(put("/api/reservas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"habitacionId\":2,\"fechaCheckOut\":\"2025-05-05\",\"metodoPago\":\"Tarjeta\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Faltan campos obligatorios en los datos."));

        verify(reservaService, never()).modificarReserva(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    public void testModificarReserva_FaltaFechaCheckOut() throws Exception {
        // Arrange
        Long id = 1L;

        // Act & Assert
        mockMvc.perform(put("/api/reservas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"habitacionId\":2,\"fechaCheckIn\":\"2025-05-01\",\"metodoPago\":\"Tarjeta\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Faltan campos obligatorios en los datos."));

        verify(reservaService, never()).modificarReserva(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    public void testModificarReserva_FaltaMetodoPago() throws Exception {
        // Arrange
        Long id = 1L;

        // Act & Assert
        mockMvc.perform(put("/api/reservas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"habitacionId\":2,\"fechaCheckIn\":\"2025-05-01\",\"fechaCheckOut\":\"2025-05-05\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Faltan campos obligatorios en los datos."));

        verify(reservaService, never()).modificarReserva(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    public void testModificarReserva_FaltaMultiplesCampos() throws Exception {
        // Arrange
        Long id = 1L;

        // Act & Assert
        mockMvc.perform(put("/api/reservas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"habitacionId\":2}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Faltan campos obligatorios en los datos."));

        verify(reservaService, never()).modificarReserva(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    public void testModificarReserva_FormatoFechaCheckInInvalido() throws Exception {
        // Arrange
        Long id = 1L;

        // Act & Assert
        mockMvc.perform(put("/api/reservas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"habitacionId\":2,\"fechaCheckIn\":\"invalid-date\",\"fechaCheckOut\":\"2025-05-05\",\"metodoPago\":\"Tarjeta\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Las fechas deben estar en formato YYYY-MM-DD."));

        verify(reservaService, never()).modificarReserva(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    public void testModificarReserva_FormatoFechaCheckOutInvalido() throws Exception {
        // Arrange
        Long id = 1L;

        // Act & Assert
        mockMvc.perform(put("/api/reservas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"habitacionId\":2,\"fechaCheckIn\":\"2025-05-01\",\"fechaCheckOut\":\"invalid-date\",\"metodoPago\":\"Tarjeta\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Las fechas deben estar en formato YYYY-MM-DD."));

        verify(reservaService, never()).modificarReserva(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    public void testModificarReserva_IdInvalido() throws Exception {
        // Arrange
        Long id = 1L;

        // Act & Assert
        mockMvc.perform(put("/api/reservas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"habitacionId\":\"invalid\",\"fechaCheckIn\":\"2025-05-01\",\"fechaCheckOut\":\"2025-05-05\",\"metodoPago\":\"Tarjeta\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El ID de la habitación debe ser un número válido."));

        verify(reservaService, never()).modificarReserva(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    public void testModificarReserva_NullHabitacionId() throws Exception {
        // Arrange
        Long id = 1L;

        // Act & Assert
        mockMvc.perform(put("/api/reservas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"habitacionId\":null,\"fechaCheckIn\":\"2025-05-01\",\"fechaCheckOut\":\"2025-05-05\",\"metodoPago\":\"Tarjeta\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Error inesperado: Cannot invoke")));

        verify(reservaService, never()).modificarReserva(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    public void testModificarReserva_NullMetodoPago() throws Exception {
        // Arrange
        Long id = 1L;

        // Act & Assert
        mockMvc.perform(put("/api/reservas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"habitacionId\":2,\"fechaCheckIn\":\"2025-05-01\",\"fechaCheckOut\":\"2025-05-05\",\"metodoPago\":null}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Error inesperado: Cannot invoke")));

        verify(reservaService, never()).modificarReserva(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    public void testModificarReserva_ErrorInesperado() throws Exception {
        // Arrange
        Long id = 1L;
        when(reservaService.modificarReserva(eq(id), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString()))
                .thenThrow(new RuntimeException("Error inesperado"));

        // Act & Assert
        mockMvc.perform(put("/api/reservas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"habitacionId\":2,\"fechaCheckIn\":\"2025-05-01\",\"fechaCheckOut\":\"2025-05-05\",\"metodoPago\":\"Tarjeta\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error inesperado: Error inesperado"));

        verify(reservaService, times(1)).modificarReserva(eq(id), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString());
    }

    @Test
    public void testCancelarReserva_Exito() throws Exception {
        // Arrange
        Long id = 1L;
        Reserva reserva = new Reserva();
        reserva.setEstado("Activa");
        when(reservaService.getReservaById(eq(id))).thenReturn(Optional.of(reserva));
        when(reservaService.cancelarReserva(id)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(put("/api/reservas/cancelar/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Reserva cancelada correctamente."));

        verify(reservaService, times(1)).getReservaById(eq(id));
        verify(reservaService, times(1)).cancelarReserva(id);
    }

    @Test
    public void testCancelarReserva_YaCancelada() throws Exception {
        // Arrange
        Long id = 1L;
        Reserva reserva = new Reserva();
        reserva.setEstado("Cancelada");
        when(reservaService.getReservaById(eq(id))).thenReturn(Optional.of(reserva));

        // Act & Assert
        mockMvc.perform(put("/api/reservas/cancelar/{id}", id))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("La reserva ya está cancelada."));

        verify(reservaService, times(1)).getReservaById(eq(id));
        verify(reservaService, never()).cancelarReserva(id);
    }

    @Test
    public void testCancelarReserva_NoEncontrada() throws Exception {
        // Arrange
        Long id = 1L;
        when(reservaService.getReservaById(eq(id))).thenReturn(Optional.empty());
        when(reservaService.cancelarReserva(id)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(put("/api/reservas/cancelar/{id}", id))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se pudo cancelar la reserva. Verifica que exista."));

        verify(reservaService, times(1)).getReservaById(eq(id));
        verify(reservaService, times(1)).cancelarReserva(id);
    }

    @Test
    public void testObtenerNuevasReservas() throws Exception {
        // Arrange
        List<Reserva> reservas = Arrays.asList(new Reserva(), new Reserva());
        when(reservaService.obtenerReservasPendientes()).thenReturn(reservas);

        // Act & Assert
        mockMvc.perform(get("/api/reservas/nuevas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(reservaService, times(1)).obtenerReservasPendientes();
    }
}
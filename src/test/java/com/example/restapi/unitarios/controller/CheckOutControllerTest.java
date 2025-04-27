package com.example.restapi.unitarios.controller;

import com.example.restapi.service.CheckOutService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.example.restapi.controller.CheckOutController;

@ExtendWith(MockitoExtension.class)
public class CheckOutControllerTest {

    @Mock
    private CheckOutService checkOutService;

    @InjectMocks
    private CheckOutController checkOutController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        // Configurar MockMvc para simular solicitudes HTTP
        mockMvc = MockMvcBuilders.standaloneSetup(checkOutController).build();
    }

    @Test
    public void testRealizarCheckOut_Success() throws Exception {
        // Arrange
        Long reservaId = 1L;
        when(checkOutService.realizarCheckOut(reservaId)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/reservas/checkout/" + reservaId))
                .andExpect(status().isOk())
                .andExpect(content().string("Check-out realizado con éxito."));

        // Verify
        verify(checkOutService, times(1)).realizarCheckOut(reservaId);
    }

    @Test
    public void testRealizarCheckOut_CheckOutFailed() throws Exception {
        // Arrange
        Long reservaId = 1L;
        when(checkOutService.realizarCheckOut(reservaId)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/reservas/checkout/" + reservaId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se pudo realizar el check-out. Verifica que la reserva exista y esté en estado 'CheckIn Realizado'."));

        // Verify
        verify(checkOutService, times(1)).realizarCheckOut(reservaId);
    }

    @Test
    public void testRealizarCheckOut_ReservaIdNull() {
        // Arrange
        Long reservaId = null;

        // Act
        ResponseEntity<String> response = checkOutController.realizarCheckOut(reservaId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El ID de la reserva es obligatorio.", response.getBody());
        verify(checkOutService, never()).realizarCheckOut(anyLong());
    }

    @Test
    public void testRealizarCheckOut_ReservaIdInvalidFormat() throws Exception {
        // Arrange
        String invalidId = "abc"; // ID no numérico

        // Act & Assert
        mockMvc.perform(post("/api/reservas/checkout/" + invalidId))
                .andExpect(status().isBadRequest()); // Spring retorna 400 por error de conversión

        // Verify
        verify(checkOutService, never()).realizarCheckOut(anyLong());
    }
}
package com.example.restapi.unitarios.controller;

import com.example.restapi.model.CheckIn;
import com.example.restapi.service.CheckInService;
import com.example.restapi.controller.CheckInController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckInControllerTest {

    @Mock
    private CheckInService checkInService;

    @InjectMocks
    private CheckInController checkInController;

    private CheckIn datosCheckIn;

    @BeforeEach
    public void setUp() {
        datosCheckIn = new CheckIn();
        datosCheckIn.setReservaId(1L);
        datosCheckIn.setNombreHuesped("Juan");
        datosCheckIn.setApellidosHuesped("Pérez");
        datosCheckIn.setDocumentoTipo("DNI");
        datosCheckIn.setDocumentoNumero("12345678");
        datosCheckIn.setTelefono("123456789");
        datosCheckIn.setEmail("juan@example.com");
        datosCheckIn.setFechaCheckIn(LocalDate.now().plusDays(1));
        datosCheckIn.setFechaCheckOut(LocalDate.now().plusDays(3));
        datosCheckIn.setNumHuespedes(2);
        datosCheckIn.setMetodoPago("Tarjeta");
    }

    @Test
    public void testRealizarCheckIn_Exitoso() {
        // Arrange
        when(checkInService.realizarCheckIn(any(CheckIn.class))).thenReturn(true);

        // Act
        ResponseEntity<String> response = checkInController.realizarCheckIn(datosCheckIn);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Check-in realizado con éxito.", response.getBody());
        verify(checkInService, times(1)).realizarCheckIn(datosCheckIn);
    }

    @Test
    public void testRealizarCheckIn_NombreHuespedNulo() {
        // Arrange
        datosCheckIn.setNombreHuesped(null);

        // Act
        ResponseEntity<String> response = checkInController.realizarCheckIn(datosCheckIn);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Faltan campos obligatorios en los datos del check-in.", response.getBody());
        verifyNoInteractions(checkInService);
    }

    @Test
    public void testRealizarCheckIn_ApellidosHuespedNulo() {
        // Arrange
        datosCheckIn.setApellidosHuesped(null);

        // Act
        ResponseEntity<String> response = checkInController.realizarCheckIn(datosCheckIn);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Faltan campos obligatorios en los datos del check-in.", response.getBody());
        verifyNoInteractions(checkInService);
    }

    @Test
    public void testRealizarCheckIn_DocumentoNumeroNulo() {
        // Arrange
        datosCheckIn.setDocumentoNumero(null);

        // Act
        ResponseEntity<String> response = checkInController.realizarCheckIn(datosCheckIn);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Faltan campos obligatorios en los datos del check-in.", response.getBody());
        verifyNoInteractions(checkInService);
    }

    @Test
    public void testRealizarCheckIn_FechaCheckInNulo() {
        // Arrange
        datosCheckIn.setFechaCheckIn(null);

        // Act
        ResponseEntity<String> response = checkInController.realizarCheckIn(datosCheckIn);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Faltan campos obligatorios en los datos del check-in.", response.getBody());
        verifyNoInteractions(checkInService);
    }

    @Test
    public void testRealizarCheckIn_FechaCheckOutNulo() {
        // Arrange
        datosCheckIn.setFechaCheckOut(null);

        // Act
        ResponseEntity<String> response = checkInController.realizarCheckIn(datosCheckIn);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Faltan campos obligatorios en los datos del check-in.", response.getBody());
        verifyNoInteractions(checkInService);
    }

    @Test
    public void testRealizarCheckIn_MetodoPagoNulo() {
        // Arrange
        datosCheckIn.setMetodoPago(null);

        // Act
        ResponseEntity<String> response = checkInController.realizarCheckIn(datosCheckIn);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Faltan campos obligatorios en los datos del check-in.", response.getBody());
        verifyNoInteractions(checkInService);
    }

    @Test
    public void testRealizarCheckIn_FechaCheckInAnterior() {
        // Arrange
        datosCheckIn.setFechaCheckIn(LocalDate.now().minusDays(1));

        // Act
        ResponseEntity<String> response = checkInController.realizarCheckIn(datosCheckIn);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("La fecha de check-in no puede ser anterior a hoy.", response.getBody());
        verifyNoInteractions(checkInService);
    }

    @Test
    public void testRealizarCheckIn_FechaCheckInHoy() {
        // Arrange
        datosCheckIn.setFechaCheckIn(LocalDate.now());
        when(checkInService.realizarCheckIn(any(CheckIn.class))).thenReturn(true);

        // Act
        ResponseEntity<String> response = checkInController.realizarCheckIn(datosCheckIn);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Check-in realizado con éxito.", response.getBody());
        verify(checkInService, times(1)).realizarCheckIn(datosCheckIn);
    }

    @Test
    public void testRealizarCheckIn_FechaCheckOutIgualCheckIn() {
        // Arrange
        datosCheckIn.setFechaCheckOut(datosCheckIn.getFechaCheckIn());

        // Act
        ResponseEntity<String> response = checkInController.realizarCheckIn(datosCheckIn);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("La fecha de check-out debe ser posterior a la fecha de check-in.", response.getBody());
        verifyNoInteractions(checkInService);
    }

    @Test
    public void testRealizarCheckIn_FechaCheckOutAnteriorCheckIn() {
        // Arrange
        datosCheckIn.setFechaCheckOut(datosCheckIn.getFechaCheckIn().minusDays(1));

        // Act
        ResponseEntity<String> response = checkInController.realizarCheckIn(datosCheckIn);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("La fecha de check-out debe ser posterior a la fecha de check-in.", response.getBody());
        verifyNoInteractions(checkInService);
    }

    @Test
    public void testRealizarCheckIn_ReservaNoValida() {
        // Arrange
        when(checkInService.realizarCheckIn(any(CheckIn.class))).thenReturn(false);

        // Act
        ResponseEntity<String> response = checkInController.realizarCheckIn(datosCheckIn);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No se pudo realizar el check-in. Verifica los datos y que la reserva exista.", response.getBody());
        verify(checkInService, times(1)).realizarCheckIn(datosCheckIn);
    }

    @Test
    public void testRealizarCheckIn_ErrorInesperado() {
        // Arrange
        when(checkInService.realizarCheckIn(any(CheckIn.class)))
                .thenThrow(new RuntimeException("Error en el servicio"));

        // Act
        ResponseEntity<String> response = checkInController.realizarCheckIn(datosCheckIn);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error inesperado: Error en el servicio", response.getBody());
        verify(checkInService, times(1)).realizarCheckIn(datosCheckIn);
    }
}
package com.example.restapi.unitarios.service;

import com.example.restapi.model.CheckIn;
import com.example.restapi.model.Reserva;
import com.example.restapi.repository.CheckInRepository;
import com.example.restapi.repository.ReservaRepository;
import com.example.restapi.service.CheckInService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckInServiceTest {

    @Mock
    private CheckInRepository checkInRepository;

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private CheckInService checkInService;

    private CheckIn checkIn;
    private Reserva reserva;

    @BeforeEach
    public void setUp() {
        checkIn = new CheckIn();
        checkIn.setReservaId(1L);
        checkIn.setNombreHuesped("Ana");
        checkIn.setApellidosHuesped("Martínez");
        checkIn.setDocumentoTipo("DNI");
        checkIn.setDocumentoNumero("12345678A");
        checkIn.setTelefono("600123456");
        checkIn.setEmail("ana.martinez@example.com");
        checkIn.setFechaCheckIn(LocalDate.of(2025, 4, 24));
        checkIn.setFechaCheckOut(LocalDate.of(2025, 4, 27));
        checkIn.setNumHuespedes(2);
        checkIn.setMetodoPago("Tarjeta");

        reserva = new Reserva();
        reserva.setId(1L);
        reserva.setEstado("Pendiente");
    }

    @Test
    public void testRealizarCheckIn_ReservaNoExiste() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.empty());

        boolean resultado = checkInService.realizarCheckIn(checkIn);

        assertFalse(resultado);
        verify(reservaRepository, times(1)).findById(1L);
        verifyNoInteractions(checkInRepository);
    }

    @Test
    public void testRealizarCheckIn_ReservaYaCheckIn() {
        reserva.setEstado("CheckIn Realizado");
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        boolean resultado = checkInService.realizarCheckIn(checkIn);

        assertFalse(resultado);
        verify(reservaRepository, times(1)).findById(1L);
        verify(reservaRepository, never()).save(any());
        verify(checkInRepository, never()).save(any());
    }

    @Test
    public void testRealizarCheckIn_Exitoso() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);
        when(checkInRepository.save(any(CheckIn.class))).thenReturn(checkIn);

        boolean resultado = checkInService.realizarCheckIn(checkIn);

        assertTrue(resultado);
        verify(reservaRepository, times(1)).findById(1L);
        verify(reservaRepository, times(1)).save(reserva);
        verify(checkInRepository, times(1)).save(checkIn);
    }

    @Test
    public void testCheckInSetAndGetNombreHuesped() {
        checkIn.setNombreHuesped("Laura");
        assertEquals("Laura", checkIn.getNombreHuesped());
    }

    @Test
    public void testCheckInSetAndGetNumHuespedes() {
        checkIn.setNumHuespedes(3);
        assertEquals(3, checkIn.getNumHuespedes());
    }
}

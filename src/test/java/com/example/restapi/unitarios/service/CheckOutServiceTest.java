package com.example.restapi.unitarios.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.restapi.model.Habitacion;
import com.example.restapi.model.Reserva;
import com.example.restapi.repository.HabitacionRepository;
import com.example.restapi.repository.ReservaRepository;
import com.example.restapi.service.CheckOutService;

@ExtendWith(MockitoExtension.class)
public class CheckOutServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private HabitacionRepository habitacionRepository;

    @InjectMocks
    private CheckOutService checkOutService;

    private Reserva reserva;
    private Habitacion habitacion;

    @BeforeEach
    public void setUp() {
        habitacion = new Habitacion();
        habitacion.setId(1L);
        habitacion.setDisponible(false);
        habitacion.setEstadoLimpieza("Limpia");

        reserva = new Reserva();
        reserva.setId(1L);
        reserva.setEstado("CheckIn Realizado");
        reserva.setHabitacion(habitacion);
    }

    @Test
    public void testRealizarCheckOut_ReservaNoExiste() {
        // Configurar mock para devolver Optional vacío
        when(reservaRepository.findById(1L)).thenReturn(Optional.empty());

        // Ejecutar el método
        boolean resultado = checkOutService.realizarCheckOut(1L);

        // Verificaciones
        assertFalse(resultado);
        verify(reservaRepository, times(1)).findById(1L);
        verifyNoInteractions(habitacionRepository);
        verify(reservaRepository, never()).save(any());
    }

    @Test
    public void testRealizarCheckOut_EstadoNoValido() {
        // Configurar reserva con estado no válido
        reserva.setEstado("Pendiente");
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        // Ejecutar el método
        boolean resultado = checkOutService.realizarCheckOut(1L);

        // Verificaciones
        assertFalse(resultado);
        verify(reservaRepository, times(1)).findById(1L);
        verify(reservaRepository, never()).save(any());
        verifyNoInteractions(habitacionRepository);
    }

    @Test
    public void testRealizarCheckOut_Exitoso() {
        // Configurar mocks para caso exitoso
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);
        when(habitacionRepository.save(any(Habitacion.class))).thenReturn(habitacion);

        // Ejecutar el método
        boolean resultado = checkOutService.realizarCheckOut(1L);

        // Verificaciones
        assertTrue(resultado);
        verify(reservaRepository, times(1)).findById(1L);
        verify(reservaRepository, times(1)).save(reserva);
        verify(habitacionRepository, times(1)).save(habitacion);
        assertEquals("CheckOut Realizado", reserva.getEstado());
        assertTrue(habitacion.isDisponible());
        assertEquals("Urgente", habitacion.getEstadoLimpieza());
    }
}
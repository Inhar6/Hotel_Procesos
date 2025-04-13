package com.example.restapi.unitarios.service;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.Habitacion;
import com.example.restapi.model.Reserva;
import com.example.restapi.repository.ClienteRepository;
import com.example.restapi.repository.HabitacionRepository;
import com.example.restapi.repository.ReservaRepository;
import com.example.restapi.service.ReservaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private HabitacionRepository habitacionRepository;

    @InjectMocks
    private ReservaService reservaService;

    private Cliente cliente;
    private Habitacion habitacion;
    private Reserva reserva;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan");
        cliente.setEmail("juan@example.com");

        habitacion = new Habitacion();
        habitacion.setId(1L);
        habitacion.setDisponible(true);
        habitacion.setPrecioPorNoche(100.0);

        reserva = new Reserva(cliente, habitacion, LocalDate.now(), LocalDate.now().plusDays(2), "Tarjeta");
        reserva.setId(1L);
    }

    @Test
    public void testReservarHabitacion() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(habitacionRepository.findById(1L)).thenReturn(Optional.of(habitacion));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        boolean result = reservaService.reservarHabitacion(1L, 1L, LocalDate.now(), LocalDate.now().plusDays(2), "Tarjeta");

        assertTrue(result);
        assertFalse(habitacion.isDisponible());
        verify(reservaRepository, times(1)).save(any(Reserva.class));
        verify(habitacionRepository, times(1)).save(habitacion);
    }

    @Test
    public void testModificarReserva() {
        Habitacion nuevaHabitacion = new Habitacion();
        nuevaHabitacion.setId(2L);
        nuevaHabitacion.setDisponible(true);
        nuevaHabitacion.setPrecioPorNoche(150.0);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(habitacionRepository.findById(2L)).thenReturn(Optional.of(nuevaHabitacion));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        boolean result = reservaService.modificarReserva(1L, 2L, LocalDate.now(), LocalDate.now().plusDays(3), "Efectivo");

        assertTrue(result);
        assertEquals(nuevaHabitacion, reserva.getHabitacion());
        assertFalse(nuevaHabitacion.isDisponible());
        verify(reservaRepository, times(1)).save(reserva);
        verify(habitacionRepository, times(1)).save(nuevaHabitacion);
    }

    @Test
    public void testCancelarReserva() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        boolean result = reservaService.cancelarReserva(1L);

        assertTrue(result);
        assertEquals("Cancelada", reserva.getEstado());
        assertTrue(habitacion.isDisponible());
        verify(reservaRepository, times(1)).save(reserva);
        verify(habitacionRepository, times(1)).save(habitacion);
    }

    @Test
    public void testGetReservasPorEmail() {
        when(reservaRepository.findByClienteEmail("juan@example.com")).thenReturn(List.of(reserva));

        List<Reserva> reservas = reservaService.getReservasPorEmail("juan@example.com");

        assertEquals(1, reservas.size());
        assertEquals(reserva, reservas.get(0));
        verify(reservaRepository, times(1)).findByClienteEmail("juan@example.com");
    }

    @Test
    public void testGetReservaById() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        Optional<Reserva> foundReserva = reservaService.getReservaById(1L);

        assertTrue(foundReserva.isPresent());
        assertEquals(reserva, foundReserva.get());
        verify(reservaRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAllReservas() {
        when(reservaRepository.findAll()).thenReturn(List.of(reserva));

        List<Reserva> reservas = reservaService.getAllReservas();

        assertEquals(1, reservas.size());
        assertEquals(reserva, reservas.get(0));
        verify(reservaRepository, times(1)).findAll();
    }
}
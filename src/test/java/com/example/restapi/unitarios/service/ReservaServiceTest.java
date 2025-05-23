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
        habitacion.setDisponible(false); // Corregido para reflejar reserva activa
        habitacion.setPrecioPorNoche(100.0);

        reserva = new Reserva(cliente, habitacion, LocalDate.now(), LocalDate.now().plusDays(2), "Tarjeta");
        reserva.setId(1L);
        reserva.setEstado("Pendiente"); // Asegurar estado Pendiente para pruebas de modificación
    }

    @Test
    public void testReservarHabitacion() {
        // Configurar habitación como disponible para este test
        habitacion.setDisponible(true);
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
    public void testReservarHabitacionClienteNoExiste() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());
        
        boolean result = reservaService.reservarHabitacion(1L, 1L, LocalDate.now(), LocalDate.now().plusDays(2), "Tarjeta");
        
        assertFalse(result);
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    public void testReservarHabitacionHabitacionNoExiste() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(habitacionRepository.findById(1L)).thenReturn(Optional.empty());
        
        boolean result = reservaService.reservarHabitacion(1L, 1L, LocalDate.now(), LocalDate.now().plusDays(2), "Tarjeta");
        
        assertFalse(result);
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    public void testReservarHabitacionNoDisponible() {
        habitacion.setDisponible(false);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(habitacionRepository.findById(1L)).thenReturn(Optional.of(habitacion));
        
        boolean result = reservaService.reservarHabitacion(1L, 1L, LocalDate.now(), LocalDate.now().plusDays(2), "Tarjeta");
        
        assertFalse(result);
        verify(reservaRepository, never()).save(any(Reserva.class));
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

        Double totalPagar = 450.0; // 3 noches * 150.0
        boolean result = reservaService.modificarReserva(1L, 2L, LocalDate.now(), LocalDate.now().plusDays(3), "Efectivo", totalPagar);

        assertTrue(result);
        assertEquals(nuevaHabitacion, reserva.getHabitacion());
        assertFalse(nuevaHabitacion.isDisponible());
        assertEquals(totalPagar, reserva.getTotalPagar());
        verify(reservaRepository, times(1)).save(reserva);
        verify(habitacionRepository, times(1)).save(nuevaHabitacion);
    }

    @Test
    public void testModificarReservaReservaNoExiste() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.empty());
        
        Double totalPagar = 450.0;
        boolean result = reservaService.modificarReserva(1L, 2L, LocalDate.now(), LocalDate.now().plusDays(3), "Efectivo", totalPagar);
        
        assertFalse(result);
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    public void testModificarReservaHabitacionNoExiste() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(habitacionRepository.findById(2L)).thenReturn(Optional.empty());
        
        Double totalPagar = 450.0;
        boolean result = reservaService.modificarReserva(1L, 2L, LocalDate.now(), LocalDate.now().plusDays(3), "Efectivo", totalPagar);
        
        assertFalse(result);
        verify(reservaRepository, never()).save(reserva);
    }

    @Test
    public void testModificarReservaMismaHabitacion() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(habitacionRepository.findById(1L)).thenReturn(Optional.of(habitacion));
        
        LocalDate nuevaFechaCheckIn = LocalDate.now().plusDays(1);
        LocalDate nuevaFechaCheckOut = LocalDate.now().plusDays(4);
        Double totalPagar = 300.0; // 3 noches * 100.0
        
        boolean result = reservaService.modificarReserva(1L, 1L, nuevaFechaCheckIn, nuevaFechaCheckOut, "Efectivo", totalPagar);
        
        assertTrue(result);
        assertEquals(nuevaFechaCheckIn, reserva.getFechaCheckIn());
        assertEquals(nuevaFechaCheckOut, reserva.getFechaCheckOut());
        assertEquals("Efectivo", reserva.getMetodoPago());
        assertEquals(totalPagar, reserva.getTotalPagar());
        verify(reservaRepository, times(1)).save(reserva);
        verify(habitacionRepository, never()).save(habitacion);
    }

    @Test
    public void testModificarReservaNuevaHabitacionNoDisponible() {
        Habitacion nuevaHabitacion = new Habitacion();
        nuevaHabitacion.setId(2L);
        nuevaHabitacion.setDisponible(false);
        
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(habitacionRepository.findById(2L)).thenReturn(Optional.of(nuevaHabitacion));
        
        Double totalPagar = 450.0;
        boolean result = reservaService.modificarReserva(1L, 2L, LocalDate.now(), LocalDate.now().plusDays(3), "Efectivo", totalPagar);
        
        assertFalse(result);
        verify(reservaRepository, never()).save(any(Reserva.class));
        verify(habitacionRepository, never()).save(any(Habitacion.class));
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
    public void testCancelarReservaNoExiste() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.empty());
        
        boolean result = reservaService.cancelarReserva(1L);
        
        assertFalse(result);
        verify(reservaRepository, never()).save(any(Reserva.class));
        verify(habitacionRepository, never()).save(any(Habitacion.class));
    }

    @Test
    public void testCancelarReservaYaCancelada() {
        reserva.cancelarReserva();
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        
        boolean result = reservaService.cancelarReserva(1L);
        
        assertFalse(result);
        verify(reservaRepository, never()).save(any(Reserva.class));
        verify(habitacionRepository, never()).save(any(Habitacion.class));
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
    public void testGetReservaByIdNoExiste() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.empty());
        
        Optional<Reserva> foundReserva = reservaService.getReservaById(1L);
        
        assertFalse(foundReserva.isPresent());
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
    
    @Test
    public void testObtenerReservasPendientes() {
        when(reservaRepository.findByEstado("Pendiente")).thenReturn(List.of(reserva));
        
        List<Reserva> reservasPendientes = reservaService.obtenerReservasPendientes();
        
        assertEquals(1, reservasPendientes.size());
        assertEquals(reserva, reservasPendientes.get(0));
        verify(reservaRepository, times(1)).findByEstado("Pendiente");
    }
}
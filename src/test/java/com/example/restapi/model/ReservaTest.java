package com.example.restapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ReservaTest {

    private Cliente cliente;
    private Habitacion habitacion;
    private Reserva reserva;

    @BeforeEach
    public void setUp() {
        cliente = new Cliente("John", "Doe", "Doe", "password", "john@example.com", "123456789");
        habitacion = new Habitacion(101, 100.0, true, "Limpia", false, "Doble");

        reserva = new Reserva(cliente, habitacion, LocalDate.of(2023, 10, 1), LocalDate.of(2023, 10, 5), "Tarjeta de Crédito");
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals(cliente, reserva.getCliente());
        assertEquals(habitacion, reserva.getHabitacion());
        assertEquals(LocalDate.of(2023, 10, 1), reserva.getFechaCheckIn());
        assertEquals(LocalDate.of(2023, 10, 5), reserva.getFechaCheckOut());
        assertEquals("Tarjeta de Crédito", reserva.getMetodoPago());
        assertEquals(400.0, reserva.getTotalPagar());
        assertEquals("Pendiente", reserva.getEstado());
    }

    @Test
    public void testSetters() {
        Cliente newCliente = new Cliente("2L", "Jane", "Smith", "newpassword", "jane@example.com", "987654321");
        Habitacion newHabitacion = new Habitacion(102, 150.0, true, "Limpia", false, "Suite");

        reserva.setCliente(newCliente);
        reserva.setHabitacion(newHabitacion);
        reserva.setFechaCheckIn(LocalDate.of(2023, 11, 1));
        reserva.setFechaCheckOut(LocalDate.of(2023, 11, 5));
        reserva.setMetodoPago("Efectivo");
        reserva.setTotalPagar(500.0);
        reserva.setEstado("Confirmada");

        assertEquals(newCliente, reserva.getCliente());
        assertEquals(newHabitacion, reserva.getHabitacion());
        assertEquals(LocalDate.of(2023, 11, 1), reserva.getFechaCheckIn());
        assertEquals(LocalDate.of(2023, 11, 5), reserva.getFechaCheckOut());
        assertEquals("Efectivo", reserva.getMetodoPago());
        assertEquals(500.0, reserva.getTotalPagar());
        assertEquals("Confirmada", reserva.getEstado());
    }

    @Test
    public void testCancelarReserva() {
        reserva.cancelarReserva();
        assertEquals("Cancelada", reserva.getEstado());
    }

    @Test
    public void testConfirmarReserva() {
        reserva.confirmarReserva();
        assertEquals("Confirmada", reserva.getEstado());
    }

    @Test
    public void testToString() {
        String expected = "Reserva{id=null, cliente=John Doe, habitacion=101, fechaCheckIn=2023-10-01, fechaCheckOut=2023-10-05, totalPagar=400.0, estado='Pendiente', metodoPago='Tarjeta de Crédito'}";
        assertEquals(expected, reserva.toString());
    }
    @Test
    public void testGetId() {
        reserva.setId(1L);
        assertEquals(1L, reserva.getId());
    }

    @Test
    public void testSetId() {
        reserva.setId(2L);
        assertEquals(2L, reserva.getId());
    }
    @Test
    public void testGetClienteId() {
        assertEquals(null, reserva.getClienteId());
    }
}
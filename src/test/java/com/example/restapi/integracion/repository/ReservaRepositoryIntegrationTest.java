package com.example.restapi.integracion.repository;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.Habitacion;
import com.example.restapi.model.Reserva;
import com.example.restapi.repository.ClienteRepository;
import com.example.restapi.repository.HabitacionRepository;
import com.example.restapi.repository.ReservaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class ReservaRepositoryIntegrationTest {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    private Cliente cliente;
    private Habitacion habitacion;
    private Reserva reserva;

    @BeforeEach
    public void setUp() {
        // Limpiar la base de datos antes de cada test
        reservaRepository.deleteAll();
        clienteRepository.deleteAll();
        habitacionRepository.deleteAll();

        // Configurar datos de prueba
        cliente = new Cliente();
        cliente.setNombre("Juan");
        cliente.setApellido("Pérez");
        cliente.setEmail("juan.perez@example.com");
        cliente.setContraseña("password123");
        cliente.setMetodoPago("Tarjeta");
        cliente.setTelefono("123456789");
        cliente = clienteRepository.save(cliente);

        habitacion = new Habitacion();
        habitacion.setDisponible(true);
        habitacion.setEstadoLimpieza("Limpia");
        habitacion.setTipo("Doble");
        habitacion.setPrecioPorNoche(100.0); // Asignar un precio por noche válido
        habitacion = habitacionRepository.save(habitacion);

        reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setHabitacion(habitacion);
        reserva.setEstado("Pendiente");
        reserva.setFechaCheckIn(LocalDate.now().plusDays(1));
        reserva.setFechaCheckOut(LocalDate.now().plusDays(3));
        reserva.setMetodoPago("Tarjeta");
        reserva.setTotalPagar(reserva.getFechaCheckIn().until(reserva.getFechaCheckOut()).getDays() * habitacion.getPrecioPorNoche());
        reserva = reservaRepository.save(reserva);
    }

    @Test
    public void testFindByCliente() {
        // Act
        List<Reserva> reservas = reservaRepository.findByCliente(cliente);

        // Assert
        assertEquals(1, reservas.size());
        assertEquals(reserva.getId(), reservas.get(0).getId());
        assertEquals(cliente.getId(), reservas.get(0).getCliente().getId());
        assertEquals("Pendiente", reservas.get(0).getEstado());
    }

    @Test
    public void testFindByCliente_NotFound() {
        // Arrange
        Cliente otroCliente = new Cliente();
        otroCliente.setNombre("Ana");
        otroCliente.setApellido("Gómez"); // Asignar un valor válido a apellido
        otroCliente.setEmail("ana@example.com");
        otroCliente.setContraseña("password456");
        otroCliente.setMetodoPago("PayPal");
        otroCliente.setTelefono("987654321");
        otroCliente = clienteRepository.save(otroCliente);

        // Act
        List<Reserva> reservas = reservaRepository.findByCliente(otroCliente);

        // Assert
        assertTrue(reservas.isEmpty());
    }

    @Test
    public void testFindByHabitacion() {
        // Act
        List<Reserva> reservas = reservaRepository.findByHabitacion(habitacion);

        // Assert
        assertEquals(1, reservas.size());
        assertEquals(reserva.getId(), reservas.get(0).getId());
        assertEquals(habitacion.getId(), reservas.get(0).getHabitacion().getId());
        assertEquals("Pendiente", reservas.get(0).getEstado());
    }

    @Test
    public void testFindByHabitacion_NotFound() {
        // Arrange
        Habitacion otraHabitacion = new Habitacion();
        otraHabitacion.setDisponible(true);
        otraHabitacion.setEstadoLimpieza("Limpia");
        otraHabitacion.setTipo("Individual");
        otraHabitacion.setPrecioPorNoche(80.0); // Asignar un precio por noche válido
        otraHabitacion = habitacionRepository.save(otraHabitacion);

        // Act
        List<Reserva> reservas = reservaRepository.findByHabitacion(otraHabitacion);

        // Assert
        assertTrue(reservas.isEmpty());
    }

    @Test
    public void testFindByEstado() {
        // Arrange
        Reserva otraReserva = new Reserva();
        otraReserva.setCliente(cliente);
        otraReserva.setHabitacion(habitacion);
        otraReserva.setEstado("CheckIn Realizado");
        otraReserva.setFechaCheckIn(LocalDate.now().plusDays(1));
        otraReserva.setFechaCheckOut(LocalDate.now().plusDays(3));
        otraReserva.setMetodoPago("Tarjeta");
        otraReserva.setTotalPagar(otraReserva.getFechaCheckIn().until(otraReserva.getFechaCheckOut()).getDays() * habitacion.getPrecioPorNoche());
        reservaRepository.save(otraReserva);

        // Act
        List<Reserva> reservas = reservaRepository.findByEstado("Pendiente");

        // Assert
        assertEquals(1, reservas.size());
        assertEquals(reserva.getId(), reservas.get(0).getId());
        assertEquals("Pendiente", reservas.get(0).getEstado());
    }

    @Test
    public void testFindByEstado_NotFound() {
        // Act
        List<Reserva> reservas = reservaRepository.findByEstado("CheckOut Realizado");

        // Assert
        assertTrue(reservas.isEmpty());
    }

    @Test
    public void testFindByClienteEmail() {
        // Act
        List<Reserva> reservas = reservaRepository.findByClienteEmail("juan.perez@example.com");

        // Assert
        assertEquals(1, reservas.size());
        assertEquals(reserva.getId(), reservas.get(0).getId());
        assertEquals(cliente.getEmail(), reservas.get(0).getCliente().getEmail());
        assertEquals("Pendiente", reservas.get(0).getEstado());
    }

    @Test
    public void testFindByClienteEmail_NotFound() {
        // Act
        List<Reserva> reservas = reservaRepository.findByClienteEmail("nonexistent@example.com");

        // Assert
        assertTrue(reservas.isEmpty());
    }
}
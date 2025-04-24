package com.example.restapi.unitarios.model;

import com.example.restapi.model.CheckIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CheckInTest {

    private CheckIn checkIn;

    @BeforeEach
    public void setUp() {
        checkIn = new CheckIn(
                1L,
                "Carlos",
                "Pérez Gómez",
                "DNI",
                "12345678X",
                "666777888",
                "carlos.perez@example.com",
                "Calle Falsa 123, Madrid",
                LocalDate.of(2023, 10, 10),
                LocalDate.of(2023, 10, 15),
                2,
                "Tarjeta de Crédito",
                "Cama doble, sin ruido",
                "Llega a las 22:00"
        );
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals(1L, checkIn.getReservaId());
        assertEquals("Carlos", checkIn.getNombreHuesped());
        assertEquals("Pérez Gómez", checkIn.getApellidosHuesped());
        assertEquals("DNI", checkIn.getDocumentoTipo());
        assertEquals("12345678X", checkIn.getDocumentoNumero());
        assertEquals("666777888", checkIn.getTelefono());
        assertEquals("carlos.perez@example.com", checkIn.getEmail());
        assertEquals("Calle Falsa 123, Madrid", checkIn.getDireccion());
        assertEquals(LocalDate.of(2023, 10, 10), checkIn.getFechaCheckIn());
        assertEquals(LocalDate.of(2023, 10, 15), checkIn.getFechaCheckOut());
        assertEquals(2, checkIn.getNumHuespedes());
        assertEquals("Tarjeta de Crédito", checkIn.getMetodoPago());
        assertEquals("Cama doble, sin ruido", checkIn.getPreferencias());
        assertEquals("Llega a las 22:00", checkIn.getComentarios());
    }

    @Test
    public void testSetters() {
        checkIn.setReservaId(2L);
        checkIn.setNombreHuesped("Lucía");
        checkIn.setApellidosHuesped("Martínez Soto");
        checkIn.setDocumentoTipo("Pasaporte");
        checkIn.setDocumentoNumero("X1234567");
        checkIn.setTelefono("911000111");
        checkIn.setEmail("lucia.martinez@example.com");
        checkIn.setDireccion("Avenida Siempre Viva 742");
        checkIn.setFechaCheckIn(LocalDate.of(2023, 11, 1));
        checkIn.setFechaCheckOut(LocalDate.of(2023, 11, 5));
        checkIn.setNumHuespedes(3);
        checkIn.setMetodoPago("Efectivo");
        checkIn.setPreferencias("Planta alta, balcón");
        checkIn.setComentarios("Necesita cuna para bebé");

        assertEquals(2L, checkIn.getReservaId());
        assertEquals("Lucía", checkIn.getNombreHuesped());
        assertEquals("Martínez Soto", checkIn.getApellidosHuesped());
        assertEquals("Pasaporte", checkIn.getDocumentoTipo());
        assertEquals("X1234567", checkIn.getDocumentoNumero());
        assertEquals("911000111", checkIn.getTelefono());
        assertEquals("lucia.martinez@example.com", checkIn.getEmail());
        assertEquals("Avenida Siempre Viva 742", checkIn.getDireccion());
        assertEquals(LocalDate.of(2023, 11, 1), checkIn.getFechaCheckIn());
        assertEquals(LocalDate.of(2023, 11, 5), checkIn.getFechaCheckOut());
        assertEquals(3, checkIn.getNumHuespedes());
        assertEquals("Efectivo", checkIn.getMetodoPago());
        assertEquals("Planta alta, balcón", checkIn.getPreferencias());
        assertEquals("Necesita cuna para bebé", checkIn.getComentarios());
    }

    @Test
    public void testSetAndGetId() {
        checkIn.setId(10L);
        assertEquals(10L, checkIn.getId());
    }

    @Test
    public void testDefaultConstructor() {
        CheckIn emptyCheckIn = new CheckIn();
        assertNull(emptyCheckIn.getId());
        assertNull(emptyCheckIn.getNombreHuesped());
        assertNull(emptyCheckIn.getEmail());
        assertNull(emptyCheckIn.getFechaCheckIn());
        assertNull(emptyCheckIn.getMetodoPago());
    }
}

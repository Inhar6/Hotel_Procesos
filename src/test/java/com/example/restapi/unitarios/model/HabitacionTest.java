
package com.example.restapi.unitarios.model;

import org.junit.jupiter.api.Test;

import com.example.restapi.model.Habitacion;

import static org.junit.jupiter.api.Assertions.*;

public class HabitacionTest {

    @Test
    public void testConstructorAndGetters() {
        Habitacion habitacion = new Habitacion(101, 150.0, true, "Limpia", false, "Doble");

        assertEquals(101, habitacion.getNumero());
        assertEquals(150.0, habitacion.getPrecioPorNoche());
        assertTrue(habitacion.isDisponible());
        assertEquals("Limpia", habitacion.getEstadoLimpieza());
        assertFalse(habitacion.isTieneProblemas());
        assertEquals("Doble", habitacion.getTipo());
    }

    @Test
    public void testSetters() {
        Habitacion habitacion = new Habitacion();

        habitacion.setNumero(102);
        habitacion.setPrecioPorNoche(200.0);
        habitacion.setDisponible(false);
        habitacion.setEstadoLimpieza("Sucia");
        habitacion.setTieneProblemas(true);
        habitacion.setTipo("Suite");

        assertEquals(102, habitacion.getNumero());
        assertEquals(200.0, habitacion.getPrecioPorNoche());
        assertFalse(habitacion.isDisponible());
        assertEquals("Sucia", habitacion.getEstadoLimpieza());
        assertTrue(habitacion.isTieneProblemas());
        assertEquals("Suite", habitacion.getTipo());
    }
    @Test
    public void testGetId() {
        Habitacion habitacion = new Habitacion();
        habitacion.setId(1L);
        assertEquals(1L, habitacion.getId());
    }

    @Test
    public void testSetId() {
        Habitacion habitacion = new Habitacion();
        habitacion.setId(2L);
        assertEquals(2L, habitacion.getId());
    }
}

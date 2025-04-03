package com.example.restapi.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.restapi.model.Habitacion;
import com.example.restapi.repository.HabitacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class HabitacionServiceTest {

    @Mock
    private HabitacionRepository habitacionRepository;

    @InjectMocks
    private HabitacionService habitacionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllHabitaciones() {
        Habitacion habitacion1 = new Habitacion(101, 100.0, true, "Limpia", false, "Individual");
        Habitacion habitacion2 = new Habitacion(102, 150.0, false, "Sucio", true, "Doble");
        when(habitacionRepository.findAll()).thenReturn(Arrays.asList(habitacion1, habitacion2));

        List<Habitacion> habitaciones = habitacionService.getAllHabitaciones();

        assertEquals(2, habitaciones.size());
        verify(habitacionRepository, times(1)).findAll();
    }

    @Test
    void testGetHabitacionById() {
        Habitacion habitacion = new Habitacion(101, 100.0, true, "Limpia", false, "Individual");
        when(habitacionRepository.findById(1L)).thenReturn(Optional.of(habitacion));

        Optional<Habitacion> foundHabitacion = habitacionService.getHabitacionById(1L);

        assertTrue(foundHabitacion.isPresent());
        assertEquals(101, foundHabitacion.get().getNumero());
        verify(habitacionRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateHabitacion() {
        Habitacion habitacion = new Habitacion(101, 100.0, true, "Limpia", false, "Individual");
        when(habitacionRepository.save(any(Habitacion.class))).thenReturn(habitacion);

        Habitacion createdHabitacion = habitacionService.createHabitacion(habitacion);

        assertNotNull(createdHabitacion);
        assertEquals(101, createdHabitacion.getNumero());
        verify(habitacionRepository, times(1)).save(habitacion);
    }

    @Test
    void testUpdateHabitacion() throws Exception {
        Habitacion existingHabitacion = new Habitacion(101, 100.0, true, "Limpia", false, "Individual");
        Habitacion updatedDetails = new Habitacion(101, 120.0, true, "Limpia", false, "Suite");
        when(habitacionRepository.findById(1L)).thenReturn(Optional.of(existingHabitacion));
        when(habitacionRepository.save(any(Habitacion.class))).thenReturn(updatedDetails);

        Habitacion updatedHabitacion = habitacionService.updateHabitacion(1L, updatedDetails);

        assertEquals(120.0, updatedHabitacion.getPrecioPorNoche());
        assertEquals("Suite", updatedHabitacion.getTipo());
        verify(habitacionRepository, times(1)).findById(1L);
        verify(habitacionRepository, times(1)).save(existingHabitacion);
    }

    @Test
    void testDeleteHabitacion() {
        when(habitacionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(habitacionRepository).deleteById(1L);

        habitacionService.deleteHabitacion(1L);

        verify(habitacionRepository, times(1)).existsById(1L);
        verify(habitacionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetHabitacionesDisponibles() {
        Habitacion habitacion1 = new Habitacion(101, 100.0, true, "Limpia", false, "Individual");
        when(habitacionRepository.findByDisponibleTrue()).thenReturn(Arrays.asList(habitacion1));

        List<Habitacion> habitacionesDisponibles = habitacionService.getHabitacionesDisponibles();

        assertEquals(1, habitacionesDisponibles.size());
        assertTrue(habitacionesDisponibles.get(0).isDisponible());
        verify(habitacionRepository, times(1)).findByDisponibleTrue();
    }

    @Test
    void testGetHabitacionesNoLimpias() {
        Habitacion habitacion1 = new Habitacion(102, 150.0, false, "Sucio", true, "Doble");
        when(habitacionRepository.findByEstadoLimpiezaNot("Limpia")).thenReturn(Arrays.asList(habitacion1));

        List<Habitacion> habitacionesNoLimpias = habitacionService.getHabitacionesNoLimpias();

        assertEquals(1, habitacionesNoLimpias.size());
        assertEquals("Sucio", habitacionesNoLimpias.get(0).getEstadoLimpieza());
        verify(habitacionRepository, times(1)).findByEstadoLimpiezaNot("Limpia");
    }
}
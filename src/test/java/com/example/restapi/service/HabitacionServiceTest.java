package com.example.restapi.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.restapi.model.Habitacion;
import com.example.restapi.repository.HabitacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class HabitacionServiceTest {

    @Mock
    private HabitacionRepository habitacionRepository;

    @InjectMocks
    private HabitacionService habitacionService;

    private Habitacion habitacion1;
    private Habitacion habitacion2;

    @BeforeEach
    void setUp() {
        habitacion1 = new Habitacion(101, 100.0, true, "Limpia", false, "Individual");
        habitacion2 = new Habitacion(102, 150.0, false, "Sucio", true, "Doble");
    }

    @Test
    void testGetAllHabitaciones() {
        when(habitacionRepository.findAll()).thenReturn(Arrays.asList(habitacion1, habitacion2));

        List<Habitacion> habitaciones = habitacionService.getAllHabitaciones();

        assertEquals(2, habitaciones.size());
        verify(habitacionRepository, times(1)).findAll();
    }

    @Test
    void testGetHabitacionById() {
        when(habitacionRepository.findById(1L)).thenReturn(Optional.of(habitacion1));

        Optional<Habitacion> foundHabitacion = habitacionService.getHabitacionById(1L);

        assertTrue(foundHabitacion.isPresent());
        assertEquals(101, foundHabitacion.get().getNumero());
        verify(habitacionRepository, times(1)).findById(1L);
    }

    @Test
    void testGetHabitacionByIdNotFound() {
        when(habitacionRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Habitacion> foundHabitacion = habitacionService.getHabitacionById(1L);

        assertFalse(foundHabitacion.isPresent());
        verify(habitacionRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateHabitacion() {
        when(habitacionRepository.save(any(Habitacion.class))).thenReturn(habitacion1);

        Habitacion createdHabitacion = habitacionService.createHabitacion(habitacion1);

        assertNotNull(createdHabitacion);
        assertEquals(101, createdHabitacion.getNumero());
        verify(habitacionRepository, times(1)).save(habitacion1);
    }

    @Test
    void testUpdateHabitacion() throws Exception {
        Habitacion updatedDetails = new Habitacion(101, 120.0, true, "Limpia", false, "Suite");
        when(habitacionRepository.findById(1L)).thenReturn(Optional.of(habitacion1));
        when(habitacionRepository.save(any(Habitacion.class))).thenReturn(updatedDetails);

        Habitacion updatedHabitacion = habitacionService.updateHabitacion(1L, updatedDetails);

        assertEquals(120.0, updatedHabitacion.getPrecioPorNoche());
        assertEquals("Suite", updatedHabitacion.getTipo());
        verify(habitacionRepository, times(1)).findById(1L);
        verify(habitacionRepository, times(1)).save(habitacion1);
    }

    @Test
    void testUpdateHabitacionNotFound() {
        Habitacion updatedDetails = new Habitacion(101, 120.0, true, "Limpia", false, "Suite");
        when(habitacionRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            habitacionService.updateHabitacion(1L, updatedDetails);
        });

        assertEquals("Habitación no encontrada", exception.getMessage());
        verify(habitacionRepository, times(1)).findById(1L);
        verify(habitacionRepository, never()).save(any(Habitacion.class));
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
    void testDeleteHabitacionNotFound() {
        when(habitacionRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(Exception.class, () -> {
            habitacionService.deleteHabitacion(1L);
        });

        assertEquals("Habitación no encontrada con id: 1", exception.getMessage());
        verify(habitacionRepository, times(1)).existsById(1L);
        verify(habitacionRepository, never()).deleteById(1L);
    }

    @Test
    void testGetHabitacionesDisponibles() {
        when(habitacionRepository.findByDisponibleTrue()).thenReturn(Arrays.asList(habitacion1));

        List<Habitacion> habitacionesDisponibles = habitacionService.getHabitacionesDisponibles();

        assertEquals(1, habitacionesDisponibles.size());
        assertTrue(habitacionesDisponibles.get(0).isDisponible());
        verify(habitacionRepository, times(1)).findByDisponibleTrue();
    }

    @Test
    void testGetHabitacionesNoLimpias() {
        when(habitacionRepository.findByEstadoLimpiezaNot("Limpia")).thenReturn(Arrays.asList(habitacion2));

        List<Habitacion> habitacionesNoLimpias = habitacionService.getHabitacionesNoLimpias();

        assertEquals(1, habitacionesNoLimpias.size());
        assertEquals("Sucio", habitacionesNoLimpias.get(0).getEstadoLimpieza());
        verify(habitacionRepository, times(1)).findByEstadoLimpiezaNot("Limpia");
    }

    @Test
    void testObtenerInformeOcupacion() {
        when(habitacionRepository.findAll()).thenReturn(Arrays.asList(habitacion1, habitacion2));

        Map<String, Object> informe = habitacionService.obtenerInformeOcupacion();

        assertNotNull(informe);
        assertEquals(2, informe.get("totalHabitaciones"));
        assertEquals(1, informe.get("habitacionesOcupadas"));
        assertEquals(1, informe.get("habitacionesDisponibles"));
        assertEquals("50,00%", informe.get("porcentajeOcupacion"));
        verify(habitacionRepository, times(1)).findAll();
    }

    @Test
    void testGetHabitacionUrgente() {
        when(habitacionRepository.findByDisponibleTrueAndEstadoLimpiezaAndTieneProblemasFalse("Limpia"))
                .thenReturn(Arrays.asList(habitacion1));

        Habitacion habitacionUrgente = habitacionService.getHabitacionUrgente();

        assertNotNull(habitacionUrgente);
        assertEquals(101, habitacionUrgente.getNumero());
        verify(habitacionRepository, times(1))
                .findByDisponibleTrueAndEstadoLimpiezaAndTieneProblemasFalse("Limpia");
    }

    @Test
    void testGetHabitacionParaLimpiezaUrgente() {
        when(habitacionRepository.findByEstadoLimpiezaNot("Limpia")).thenReturn(Arrays.asList(habitacion2));

        Habitacion habitacionParaLimpiezaUrgente = habitacionService.getHabitacionParaLimpiezaUrgente();

        assertNotNull(habitacionParaLimpiezaUrgente);
        assertEquals("Sucio", habitacionParaLimpiezaUrgente.getEstadoLimpieza());
        verify(habitacionRepository, times(1)).findByEstadoLimpiezaNot("Limpia");
    }

    @Test
    void testGetHabitacionesUrgentes() {
        when(habitacionRepository.findByDisponibleTrueAndEstadoLimpiezaAndTieneProblemasFalse("Limpia"))
                .thenReturn(Arrays.asList(habitacion1));

        List<Habitacion> habitacionesUrgentes = habitacionService.getHabitacionesUrgentes();

        assertEquals(1, habitacionesUrgentes.size());
        assertEquals(101, habitacionesUrgentes.get(0).getNumero());
        verify(habitacionRepository, times(1))
                .findByDisponibleTrueAndEstadoLimpiezaAndTieneProblemasFalse("Limpia");
    }
}
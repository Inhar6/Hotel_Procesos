package com.example.restapi.unitarios.controller;

import com.example.restapi.controller.HabitacionController;
import com.example.restapi.model.Habitacion;
import com.example.restapi.service.HabitacionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HabitacionControllerTest {

    @Mock
    private HabitacionService habitacionService;

    @InjectMocks
    private HabitacionController habitacionController;

    private Habitacion habitacion;

    @BeforeEach
    public void setUp() {
        habitacion = new Habitacion();
        habitacion.setId(1L);
        habitacion.setTipo("Doble");
        habitacion.setPrecioPorNoche(100.0);
        habitacion.setDisponible(true);
        habitacion.setEstadoLimpieza("Limpia");
    }

    @Test
    public void testGetAllHabitaciones() {
        // Arrange
        List<Habitacion> habitaciones = Arrays.asList(habitacion);
        when(habitacionService.getAllHabitaciones()).thenReturn(habitaciones);

        // Act
        List<Habitacion> result = habitacionController.getAllHabitaciones();

        // Assert
        assertEquals(habitaciones, result);
        verify(habitacionService, times(1)).getAllHabitaciones();
    }

    @Test
    public void testGetHabitacionesDisponibles() {
        // Arrange
        List<Habitacion> habitaciones = Arrays.asList(habitacion);
        when(habitacionService.getHabitacionesDisponibles()).thenReturn(habitaciones);

        // Act
        List<Habitacion> result = habitacionController.getHabitacionesDisponibles();

        // Assert
        assertEquals(habitaciones, result);
        verify(habitacionService, times(1)).getHabitacionesDisponibles();
    }

    @Test
    public void testGetHabitacionesDisponiblesPorTipo_ConResultados() {
        // Arrange
        List<Habitacion> habitaciones = Arrays.asList(habitacion);
        when(habitacionService.getHabitacionesDisponibles()).thenReturn(habitaciones);

        // Act
        List<Habitacion> result = habitacionController.getHabitacionesDisponiblesPorTipo("Doble");

        // Assert
        assertEquals(1, result.size());
        assertEquals(habitacion, result.get(0));
        verify(habitacionService, times(1)).getHabitacionesDisponibles();
    }

    @Test
    public void testGetHabitacionesDisponiblesPorTipo_SinResultados() {
        // Arrange
        List<Habitacion> habitaciones = Arrays.asList(habitacion);
        when(habitacionService.getHabitacionesDisponibles()).thenReturn(habitaciones);

        // Act
        List<Habitacion> result = habitacionController.getHabitacionesDisponiblesPorTipo("Individual");

        // Assert
        assertTrue(result.isEmpty());
        verify(habitacionService, times(1)).getHabitacionesDisponibles();
    }

    @Test
    public void testGetHabitacionesDisponiblesPorPrecio_ConResultados() {
        // Arrange
        List<Habitacion> habitaciones = Arrays.asList(habitacion);
        when(habitacionService.getHabitacionesDisponibles()).thenReturn(habitaciones);

        // Act
        List<Habitacion> result = habitacionController.getHabitacionesDisponiblesPorPrecio(150.0);

        // Assert
        assertEquals(1, result.size());
        assertEquals(habitacion, result.get(0));
        verify(habitacionService, times(1)).getHabitacionesDisponibles();
    }

    @Test
    public void testGetHabitacionesDisponiblesPorPrecio_SinResultados() {
        // Arrange
        List<Habitacion> habitaciones = Arrays.asList(habitacion);
        when(habitacionService.getHabitacionesDisponibles()).thenReturn(habitaciones);

        // Act
        List<Habitacion> result = habitacionController.getHabitacionesDisponiblesPorPrecio(50.0);

        // Assert
        assertTrue(result.isEmpty());
        verify(habitacionService, times(1)).getHabitacionesDisponibles();
    }

    @Test
    public void testGetHabitacionUrgente_Encontrada() {
        // Arrange
        when(habitacionService.getHabitacionUrgente()).thenReturn(habitacion);

        // Act
        Habitacion result = habitacionController.getHabitacionUrgente();

        // Assert
        assertEquals(habitacion, result);
        verify(habitacionService, times(1)).getHabitacionUrgente();
    }

    @Test
    public void testGetHabitacionUrgente_NoEncontrada() {
        // Arrange
        when(habitacionService.getHabitacionUrgente()).thenReturn(null);

        // Act
        Habitacion result = habitacionController.getHabitacionUrgente();

        // Assert
        assertNull(result);
        verify(habitacionService, times(1)).getHabitacionUrgente();
    }

    @Test
    public void testGetHabitacionesUrgentes() {
        // Arrange
        List<Habitacion> habitaciones = Arrays.asList(habitacion);
        when(habitacionService.getHabitacionesUrgentes()).thenReturn(habitaciones);

        // Act
        List<Habitacion> result = habitacionController.getHabitacionesUrgentes();

        // Assert
        assertEquals(habitaciones, result);
        verify(habitacionService, times(1)).getHabitacionesUrgentes();
    }

    @Test
    public void testGetHabitacionesNoLimpias() {
        // Arrange
        List<Habitacion> habitaciones = Arrays.asList(habitacion);
        when(habitacionService.getHabitacionesNoLimpias()).thenReturn(habitaciones);

        // Act
        List<Habitacion> result = habitacionController.getHabitacionesNoLimpias();

        // Assert
        assertEquals(habitaciones, result);
        verify(habitacionService, times(1)).getHabitacionesNoLimpias();
    }

    @Test
    public void testMarcarHabitacionComoLimpia_Exito() {
        // Arrange
        when(habitacionService.marcarHabitacionComoLimpia(1L)).thenReturn(habitacion);

        // Act
        ResponseEntity<Habitacion> response = habitacionController.marcarHabitacionComoLimpia(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(habitacion, response.getBody());
        verify(habitacionService, times(1)).marcarHabitacionComoLimpia(1L);
    }

    @Test
    public void testMarcarHabitacionComoLimpia_NoEncontrada() {
        // Arrange
        when(habitacionService.marcarHabitacionComoLimpia(1L))
                .thenThrow(new RuntimeException("Habitación no encontrada"));

        // Act
        ResponseEntity<Habitacion> response = habitacionController.marcarHabitacionComoLimpia(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(habitacionService, times(1)).marcarHabitacionComoLimpia(1L);
    }

    @Test
    public void testGetHabitacionParaLimpiezaUrgente_Encontrada() {
        // Arrange
        when(habitacionService.getHabitacionParaLimpiezaUrgente()).thenReturn(habitacion);

        // Act
        Habitacion result = habitacionController.getHabitacionParaLimpiezaUrgente();

        // Assert
        assertEquals(habitacion, result);
        verify(habitacionService, times(1)).getHabitacionParaLimpiezaUrgente();
    }

    @Test
    public void testGetHabitacionParaLimpiezaUrgente_NoEncontrada() {
        // Arrange
        when(habitacionService.getHabitacionParaLimpiezaUrgente()).thenReturn(null);

        // Act
        Habitacion result = habitacionController.getHabitacionParaLimpiezaUrgente();

        // Assert
        assertNull(result);
        verify(habitacionService, times(1)).getHabitacionParaLimpiezaUrgente();
    }

    @Test
    public void testObtenerOcupacionHotel() {
        // Arrange
        Map<String, Object> informe = new HashMap<>();
        informe.put("ocupacion", 75.0);
        when(habitacionService.obtenerInformeOcupacion()).thenReturn(informe);

        // Act
        Map<String, Object> result = habitacionController.obtenerOcupacionHotel();

        // Assert
        assertEquals(informe, result);
        verify(habitacionService, times(1)).obtenerInformeOcupacion();
    }
}
package com.example.restapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ProblemaTest {

   private Problema problema;

   @BeforeEach
   void setUp() {
      problema = new Problema();
   }

   @Test
   void testSettersAndGetters() {
      Long id = 1L;
      String nombre = "Juan Pérez";
      String email = "juan@example.com";
      String numeroHabitacion = "101";
      LocalDate fechaReporte = LocalDate.now();
      String descripcion = "Problema con la ducha.";
      String sugerencias = "Reemplazar grifo.";
      String urgencia = "Alta";
      boolean resuelto = false;

      problema.setId(id);
      problema.setNombre(nombre);
      problema.setEmail(email);
      problema.setNumeroHabitacion(numeroHabitacion);
      problema.setFechaReporte(fechaReporte);
      problema.setDescripcion(descripcion);
      problema.setSugerencias(sugerencias);
      problema.setUrgencia(urgencia);
      problema.setResuelto(resuelto);

      assertEquals(id, problema.getId());
      assertEquals(nombre, problema.getNombre());
      assertEquals(email, problema.getEmail());
      assertEquals(numeroHabitacion, problema.getNumeroHabitacion());
      assertEquals(fechaReporte, problema.getFechaReporte());
      assertEquals(descripcion, problema.getDescripcion());
      assertEquals(sugerencias, problema.getSugerencias());
      assertEquals(urgencia, problema.getUrgencia());
      assertEquals(resuelto, problema.isResuelto());
   }

   @Test
   void testDefaultValues() {
      Problema nuevoProblema = new Problema();
      assertFalse(nuevoProblema.isResuelto(), "Por defecto el problema debería estar como no resuelto (false).");
   }
}

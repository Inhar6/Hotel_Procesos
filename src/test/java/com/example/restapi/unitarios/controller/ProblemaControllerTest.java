package com.example.restapi.unitarios.controller;

import com.example.restapi.controller.ProblemaController;
import com.example.restapi.model.Problema;
import com.example.restapi.service.ProblemaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProblemaControllerTest {

   @Mock
   private ProblemaService problemaService;

   @InjectMocks
   private ProblemaController problemaController;

   private Problema problema;

   @BeforeEach
   void setUp() {
      MockitoAnnotations.openMocks(this);

      problema = new Problema();
      problema.setId(1L);
      problema.setNombre("Juan Pérez");
      problema.setEmail("juan@example.com");
      problema.setNumeroHabitacion("101");
      problema.setDescripcion("Problema con la ducha");
      problema.setUrgencia("Alta");
      problema.setResuelto(false);
   }

   @Test
   void testReportarProblema() {
      when(problemaService.reportarProblema(any(Problema.class))).thenReturn(problema);

      ResponseEntity<Problema> response = problemaController.reportarProblema(problema);

      assertEquals(200, response.getStatusCodeValue());
      assertEquals(problema, response.getBody());
      verify(problemaService, times(1)).reportarProblema(problema);
   }

   @Test
   void testObtenerProblemasNoResueltos() {
      List<Problema> problemas = Arrays.asList(problema);
      when(problemaService.obtenerProblemasNoResueltos()).thenReturn(problemas);

      ResponseEntity<List<Problema>> response = problemaController.obtenerProblemasNoResueltos();

      assertEquals(200, response.getStatusCodeValue());
      assertEquals(1, response.getBody().size());
      assertEquals(problema, response.getBody().get(0));
      verify(problemaService, times(1)).obtenerProblemasNoResueltos();
   }

   @Test
   void testResolverProblemaExistente() {
      when(problemaService.obtenerProblemaPorId(1L)).thenReturn(Optional.of(problema));

      ResponseEntity<String> response = problemaController.resolverProblema(1L);

      assertEquals(200, response.getStatusCodeValue());
      assertEquals("Problema marcado como resuelto.", response.getBody());
      assertTrue(problema.isResuelto());
      verify(problemaService, times(1)).guardarProblema(problema);
   }

   @Test
   void testResolverProblemaNoExistente() {
      when(problemaService.obtenerProblemaPorId(2L)).thenReturn(Optional.empty());

      ResponseEntity<String> response = problemaController.resolverProblema(2L);

      assertEquals(404, response.getStatusCodeValue());
      verify(problemaService, never()).guardarProblema(any());
   }
}

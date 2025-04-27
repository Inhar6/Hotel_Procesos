package com.example.restapi.unitarios.service;

import com.example.restapi.model.Problema;
import com.example.restapi.repository.ProblemaRepository;
import com.example.restapi.service.ProblemaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProblemaServiceTest {

   @Mock
   private ProblemaRepository problemaRepository;

   @InjectMocks
   private ProblemaService problemaService;

   private Problema problema;

   @BeforeEach
   void setUp() {
      MockitoAnnotations.openMocks(this);

      problema = new Problema();
      problema.setNombre("Juan Pérez");
      problema.setEmail("juan@example.com");
      problema.setNumeroHabitacion("101");
      problema.setDescripcion("Problema con el grifo");
      problema.setUrgencia("Alta");
   }

   @Test
   void testReportarProblema() {
      when(problemaRepository.save(any(Problema.class))).thenAnswer(i -> i.getArgument(0));

      Problema resultado = problemaService.reportarProblema(problema);

      assertNotNull(resultado);
      assertNotNull(resultado.getFechaReporte());
      assertFalse(resultado.isResuelto());
      verify(problemaRepository, times(1)).save(problema);
   }

   @Test
   void testObtenerProblemasNoResueltos() {
      when(problemaRepository.findByResueltoFalse()).thenReturn(Arrays.asList(problema));

      List<Problema> problemas = problemaService.obtenerProblemasNoResueltos();

      assertNotNull(problemas);
      assertEquals(1, problemas.size());
      verify(problemaRepository, times(1)).findByResueltoFalse();
   }

   @Test
   void testObtenerProblemaPorIdExistente() {
      when(problemaRepository.findById(1L)).thenReturn(Optional.of(problema));

      Optional<Problema> encontrado = problemaService.obtenerProblemaPorId(1L);

      assertTrue(encontrado.isPresent());
      assertEquals("Juan Pérez", encontrado.get().getNombre());
      verify(problemaRepository, times(1)).findById(1L);
   }

   @Test
   void testObtenerProblemaPorIdNoExistente() {
      when(problemaRepository.findById(2L)).thenReturn(Optional.empty());

      Optional<Problema> encontrado = problemaService.obtenerProblemaPorId(2L);

      assertFalse(encontrado.isPresent());
      verify(problemaRepository, times(1)).findById(2L);
   }

   @Test
   void testGuardarProblema() {
      problemaService.guardarProblema(problema);

      verify(problemaRepository, times(1)).save(problema);
   }
}

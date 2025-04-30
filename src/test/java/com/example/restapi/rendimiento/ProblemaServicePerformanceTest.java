package com.example.restapi.rendimiento;

import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.example.restapi.model.Problema;
import com.example.restapi.service.ProblemaService;
import com.example.restapi.repository.ProblemaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProblemaServicePerformanceTest {

    @Autowired
    private ProblemaService problemaService;

    @MockBean
    private ProblemaRepository problemaRepository;

    private Problema testProblema;

    @BeforeEach
    public void setUp() {
        testProblema = new Problema();
        testProblema.setDescripcion("Test problema");
        testProblema.setId(1L);

        when(problemaRepository.save(testProblema)).thenReturn(testProblema);
        when(problemaRepository.findByResueltoFalse()).thenReturn(Collections.singletonList(testProblema));
        when(problemaRepository.findById(1L)).thenReturn(Optional.of(testProblema));
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 10000)
    @JUnitPerfTestRequirement(executionsPerSec = 50, allowedErrorPercentage = (float) 0.1)
    public void testReportarProblemaPerformance() {
        Problema result = problemaService.reportarProblema(testProblema);
        assertTrue(result != null && result.getFechaReporte() != null, "Problema should be reported with a date");
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 10000)
    @JUnitPerfTestRequirement(executionsPerSec = 50, allowedErrorPercentage = (float) 0.1)
    public void testObtenerProblemasNoResueltosPerformance() {
        List<Problema> problemas = problemaService.obtenerProblemasNoResueltos();
        assertTrue(problemas.size() > 0, "There should be unresolved problems");
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 10000)
    @JUnitPerfTestRequirement(executionsPerSec = 50, allowedErrorPercentage = (float) 0.1)
    public void testObtenerProblemaPorIdPerformance() {
        Optional<Problema> problema = problemaService.obtenerProblemaPorId(1L);
        assertTrue(problema.isPresent(), "Problema should be present");
    }
}

package com.example.restapi.rendimiento;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.example.restapi.model.Problema;
import com.example.restapi.service.ProblemaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
/* 
@SpringBootTest
public class ProblemaServicePerformanceTest {

    @Autowired
    private ProblemaService problemaService;

    private Problema testProblema;

    @BeforeEach
    public void setUp() {
        testProblema = new Problema();
        testProblema.setDescripcion("Test problema");
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 10000)
    @JUnitPerfTestRequirement(executionsPerSec = 50, allowedErrorPercentage = (float) 0.1)
    public void testReportarProblemaPerformance() {
        problemaService.reportarProblema(testProblema);
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 10000)
    @JUnitPerfTestRequirement(executionsPerSec = 50, allowedErrorPercentage = (float) 0.1)
    public void testObtenerProblemasNoResueltosPerformance() {
        problemaService.obtenerProblemasNoResueltos();
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 10000)
    @JUnitPerfTestRequirement(executionsPerSec = 50, allowedErrorPercentage = (float) 0.1)
    public void testObtenerProblemaPorIdPerformance() {
        Optional<Problema> problema = problemaService.obtenerProblemaPorId(1L);
        // Optionally assert the result if needed
        assertTrue(problema.isPresent(), "Problema should be present");
    }
}*/
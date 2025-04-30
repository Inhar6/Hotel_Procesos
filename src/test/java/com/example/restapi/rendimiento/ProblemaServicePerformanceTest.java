package com.example.restapi.rendimiento;

import com.example.restapi.model.Problema;
import com.example.restapi.service.ProblemaService;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/* 
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ProblemaServicePerformanceTest {

    @Autowired
    private ProblemaService problemaService;

    private Problema problemaDePrueba;

    @BeforeEach
    public void setup() {
        problemaDePrueba = new Problema();
        problemaDePrueba.setNombre("Usuario Prueba");
        problemaDePrueba.setEmail("usuario@example.com");
        problemaDePrueba.setNumeroHabitacion("101");
        problemaDePrueba.setDescripcion("Problema de prueba - " + UUID.randomUUID());
        problemaDePrueba.setSugerencias("Reiniciar sistema");
        problemaDePrueba.setUrgencia("Media");
    }
    @Test
    @JUnitPerfTest(threads = 5, durationMs = 3000, warmUpMs = 500)
    @JUnitPerfTestRequirement(allowedErrorPercentage = 5)
    public void testReportarProblemaPerformance() {
        problemaService.reportarProblema(problemaDePrueba);
    }
}*/
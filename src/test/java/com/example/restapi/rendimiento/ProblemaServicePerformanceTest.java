package com.example.restapi.rendimiento;

import com.example.restapi.model.Problema;
import com.example.restapi.service.ProblemaService;
import com.github.noconnor.junitperf.JUnitPerfInterceptor;
import com.github.noconnor.junitperf.JUnitPerfReportingConfig;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestActiveConfig;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
@ExtendWith(JUnitPerfInterceptor.class)
public class ProblemaServicePerformanceTest {

    @Autowired
    private ProblemaService problemaService;

    private Problema problemaDePrueba;

    @JUnitPerfTestActiveConfig
    private static final JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
            .reportGenerator(new HtmlReportGenerator(System.getProperty("user.dir") + "/target/reports/problemaPerf-report.html"))
            .build();

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
    @JUnitPerfTestRequirement(allowedErrorPercentage = 0.05f)
    public void testReportarProblemaPerformance() {
        problemaService.reportarProblema(problemaDePrueba);
    }
}
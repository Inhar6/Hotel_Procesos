package com.example.restapi.rendimiento;

import com.example.restapi.model.Reserva;
import com.example.restapi.service.ReservaService;
import com.github.noconnor.junitperf.JUnitPerfInterceptor;
import com.github.noconnor.junitperf.JUnitPerfReportingConfig;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestActiveConfig;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@ExtendWith(JUnitPerfInterceptor.class)
public class ReservaServiceListPerformanceTest {

    @Autowired
    private ReservaService reservaService;

    @JUnitPerfTestActiveConfig
    private static final JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
            .reportGenerator(new HtmlReportGenerator(System.getProperty("user.dir") + "/target/reports/reservaListPerf-report.html"))
            .build();

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 3000, warmUpMs = 500)
    @JUnitPerfTestRequirement(allowedErrorPercentage = 0.05f)
    public void testListarReservasPerformance() {
        List<Reserva> reservas = reservaService.getAllReservas();
    }
}
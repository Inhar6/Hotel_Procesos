package com.example.restapi.unitarios.service;

import com.example.restapi.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private Environment env;

    @InjectMocks
    private EmailService emailService;

    private final String destinatario = "cliente@example.com";
    private final String nombreCliente = "Juan Pérez";
    private final String habitacion = "305";
    private final String fecha = "2025-05-17";

    @BeforeEach
    void setUp() {
        when(env.getProperty("spring.mail.username")).thenReturn("hotel@example.com");
    }

    @Test
    void testEnviarConfirmacionCheckIn() {
        // Act
        emailService.enviarConfirmacionCheckIn(destinatario, nombreCliente, habitacion, fecha);

        // Assert
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        SimpleMailMessage mensajeEnviado = captor.getValue();

        assertNotNull(mensajeEnviado, "El mensaje no debe ser nulo");
        assertNotNull(mensajeEnviado.getTo(), "El destinatario no debe ser nulo");
        assertEquals(1, mensajeEnviado.getTo().length, "Debe haber un solo destinatario");
        assertEquals(destinatario, mensajeEnviado.getTo()[0]);

        assertEquals("Confirmación de Check-In", mensajeEnviado.getSubject());
        assertTrue(mensajeEnviado.getText().contains(nombreCliente));
        assertTrue(mensajeEnviado.getText().contains(habitacion));
        assertTrue(mensajeEnviado.getText().contains(fecha));
        assertEquals("hotel@example.com", mensajeEnviado.getFrom());
    }
}

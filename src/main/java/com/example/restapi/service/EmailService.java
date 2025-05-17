package com.example.restapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

   @Autowired
   private JavaMailSender mailSender;

   @Autowired
   private Environment env;

   public void enviarConfirmacionCheckIn(String destinatario, String nombreCliente, String habitacion, String fecha) {
      SimpleMailMessage mensaje = new SimpleMailMessage();
      mensaje.setTo(destinatario);
      mensaje.setSubject("Confirmación de Check-In");
      mensaje.setText("Hola " + nombreCliente + ",\n\nTu check-in ha sido registrado correctamente para la habitación " +
         habitacion + " en la fecha " + fecha + ".\n\nGracias por confiar en nosotros.\n\nHotel SPA S.L.");
      mensaje.setFrom(env.getProperty("spring.mail.username"));
      mailSender.send(mensaje);
   }
}

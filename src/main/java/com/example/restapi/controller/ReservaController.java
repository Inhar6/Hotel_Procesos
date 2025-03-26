package com.example.restapi.controller;

import com.example.restapi.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    @Autowired
    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    // Endpoint para hacer una reserva
    @PostMapping
    public ResponseEntity<String> hacerReserva(@RequestBody Map<String, Object> datos) {
        try {
            Long clienteId = Long.parseLong(datos.get("clienteId").toString());
            Long habitacionId = Long.parseLong(datos.get("habitacionId").toString());
            LocalDate fechaCheckIn = LocalDate.parse(datos.get("fechaCheckIn").toString());
            LocalDate fechaCheckOut = LocalDate.parse(datos.get("fechaCheckOut").toString());
            String metodoPago = datos.get("metodoPago").toString();

            boolean exito = reservaService.reservarHabitacion(clienteId, habitacionId, fechaCheckIn, fechaCheckOut, metodoPago);

            if (exito) {
                return ResponseEntity.ok("Reserva realizada con éxito.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo realizar la reserva.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en los datos de la reserva.");
        }
    }

    // Endpoint para cancelar una reserva
    @PutMapping("/cancelar/{id}")
    public ResponseEntity<String> cancelarReserva(@PathVariable Long id) {
        boolean exito = reservaService.cancelarReserva(id);
        if (exito) {
            return ResponseEntity.ok("Reserva cancelada correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo cancelar la reserva.");
        }
    }
}
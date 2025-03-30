package com.example.restapi.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.time.format.DateTimeParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restapi.model.Reserva;
import com.example.restapi.service.ReservaService;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    @Autowired
    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    // Endpoint para obtener todas las reservas    
    @GetMapping
    public List<Reserva> getAllReservas() {
        return reservaService.getAllReservas();
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

    // Endpoint para modificar una reserva
    @PutMapping("/{id}")
    public ResponseEntity<String> modificarReserva(
            @PathVariable Long id,
            @RequestBody Map<String, Object> datos) {
        try {
            // Validar que todos los campos estén presentes
            if (!datos.containsKey("habitacionId") || !datos.containsKey("fechaCheckIn")
                    || !datos.containsKey("fechaCheckOut") || !datos.containsKey("metodoPago")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Faltan campos obligatorios en los datos.");
            }

            Long habitacionId = Long.parseLong(datos.get("habitacionId").toString());
            LocalDate fechaCheckIn = LocalDate.parse(datos.get("fechaCheckIn").toString());
            LocalDate fechaCheckOut = LocalDate.parse(datos.get("fechaCheckOut").toString());
            String metodoPago = datos.get("metodoPago").toString();

            boolean exito = reservaService.modificarReserva(id, habitacionId, fechaCheckIn, fechaCheckOut, metodoPago);

            if (exito) {
                return ResponseEntity.ok("Reserva modificada con éxito.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo modificar la reserva. Verifica que la reserva exista y la habitación esté disponible.");
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID de la habitación debe ser un número válido.");
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Las fechas deben estar en formato YYYY-MM-DD.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error inesperado: " + e.getMessage());
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

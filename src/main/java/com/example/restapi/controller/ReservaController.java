package com.example.restapi.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restapi.model.Reserva;
import com.example.restapi.service.ReservaService;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public List<Reserva> getAllReservas() {
        return reservaService.getAllReservas();
    }

    @GetMapping("/por-email")
    public ResponseEntity<List<Reserva>> getReservasPorEmail(@RequestParam String email) {
        List<Reserva> reservas = reservaService.getReservasPorEmail(email);
        if (reservas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> getReservaById(@PathVariable Long id) {
        Optional<Reserva> reserva = reservaService.getReservaById(id);
        return reserva.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<String> hacerReserva(@RequestBody Map<String, Object> datos) {
        try {
            if (!datos.containsKey("clienteId") || datos.get("clienteId") == null ||
                !datos.containsKey("habitacionId") || datos.get("habitacionId") == null ||
                !datos.containsKey("fechaCheckIn") || datos.get("fechaCheckIn") == null ||
                !datos.containsKey("fechaCheckOut") || datos.get("fechaCheckOut") == null ||
                !datos.containsKey("metodoPago") || datos.get("metodoPago") == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Error en los datos de la reserva.");
            }

            Long clienteId = Long.parseLong(datos.get("clienteId").toString());
            Long habitacionId = Long.parseLong(datos.get("habitacionId").toString());
            LocalDate fechaCheckIn = LocalDate.parse(datos.get("fechaCheckIn").toString());
            LocalDate fechaCheckOut = LocalDate.parse(datos.get("fechaCheckOut").toString());
            String metodoPago = datos.get("metodoPago").toString();

            boolean exito = reservaService.reservarHabitacion(clienteId, habitacionId, fechaCheckIn, fechaCheckOut, metodoPago);

            if (exito) {
                return ResponseEntity.ok("Reserva realizada con éxito.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No se pudo realizar la reserva.");
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error en los datos de la reserva.");
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error en los datos de la reserva.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error en los datos de la reserva.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> modificarReserva(
            @PathVariable Long id,
            @RequestBody Map<String, Object> datos) {
        try {
            if (!datos.containsKey("habitacionId") || datos.get("habitacionId") == null ||
                !datos.containsKey("fechaCheckIn") || datos.get("fechaCheckIn") == null ||
                !datos.containsKey("fechaCheckOut") || datos.get("fechaCheckOut") == null ||
                !datos.containsKey("metodoPago") || datos.get("metodoPago") == null ||
                !datos.containsKey("totalPagar") || datos.get("totalPagar") == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Faltan campos obligatorios en los datos.");
            }

            Long habitacionId;
            try {
                habitacionId = Long.parseLong(datos.get("habitacionId").toString());
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El ID de la habitación debe ser un número válido.");
            }

            LocalDate fechaCheckIn;
            try {
                fechaCheckIn = LocalDate.parse(datos.get("fechaCheckIn").toString());
            } catch (DateTimeParseException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Las fechas deben estar en formato YYYY-MM-DD.");
            }

            LocalDate fechaCheckOut;
            try {
                fechaCheckOut = LocalDate.parse(datos.get("fechaCheckOut").toString());
            } catch (DateTimeParseException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Las fechas deben estar en formato YYYY-MM-DD.");
            }

            String metodoPago = datos.get("metodoPago").toString();

            Double totalPagar;
            try {
                totalPagar = Double.parseDouble(datos.get("totalPagar").toString());
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El totalPagar debe ser un número válido.");
            }

            boolean exito = reservaService.modificarReserva(id, habitacionId, fechaCheckIn, fechaCheckOut, metodoPago, totalPagar);

            if (exito) {
                return ResponseEntity.ok("Reserva modificada con éxito.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No se pudo modificar la reserva. Verifica que la reserva exista y la habitación esté disponible.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error inesperado: " + e.getMessage());
        }
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<String> cancelarReserva(@PathVariable Long id) {
        Optional<Reserva> reservaOpt = reservaService.getReservaById(id);
        if (reservaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se pudo cancelar la reserva. Verifica que exista.");
        }

        Reserva reserva = reservaOpt.get();
        if ("Cancelada".equalsIgnoreCase(reserva.getEstado())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("La reserva ya está cancelada.");
        }

        boolean exito = reservaService.cancelarReserva(id);
        if (exito) {
            return ResponseEntity.ok("Reserva cancelada correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se pudo cancelar la reserva. Verifica que exista.");
        }
    }

    @GetMapping("/nuevas")
    public List<Reserva> obtenerNuevasReservas() {
        return reservaService.obtenerReservasPendientes();
    }
}
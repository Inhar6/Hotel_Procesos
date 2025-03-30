package com.example.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.restapi.service.CheckOutService;

@RestController
@RequestMapping("/api/reservas")
public class CheckOutController {

    private final CheckOutService checkOutService;

    @Autowired
    public CheckOutController(CheckOutService checkOutService) {
        this.checkOutService = checkOutService;
    }

    @PostMapping("/checkout/{reservaId}")
    public ResponseEntity<String> realizarCheckOut(@PathVariable("reservaId") Long reservaId) {
        if (reservaId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID de la reserva es obligatorio.");
        }

        boolean exito = checkOutService.realizarCheckOut(reservaId);

        if (exito) {
            return ResponseEntity.ok("Check-out realizado con éxito.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo realizar el check-out. Verifica que la reserva exista y esté en estado 'CheckIn Realizado'.");
        }
    }
}
package com.example.restapi.controller;

import com.example.restapi.model.Habitacion;
import com.example.restapi.service.HabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habitacion")
public class HabitacionController {

    private final HabitacionService habitacionService;

    @Autowired
    public HabitacionController(HabitacionService habitacionService) {
        this.habitacionService = habitacionService;
    }

    @GetMapping("/lista")
    public List<Habitacion> getAllHabitaciones() {
        return habitacionService.getAllHabitaciones();
    }
}
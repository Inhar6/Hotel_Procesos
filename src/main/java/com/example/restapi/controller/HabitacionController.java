package com.example.restapi.controller;

import com.example.restapi.model.Habitacion;
import com.example.restapi.service.HabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/personal")
public class HabitacionController {

    private final HabitacionService habitacionService;

    @Autowired
    public HabitacionController(HabitacionService habitacionService) {
        this.habitacionService = habitacionService;
    }

    @GetMapping("/habitaciones")
    public List<Habitacion> getAllHabitaciones() {
        return habitacionService.getAllHabitaciones();
    }

    //Habitaciones Libres
    @GetMapping("/habitaciones/disponibles")
    public List<Habitacion> getHabitacionesDisponibles() {
        return habitacionService.getHabitacionesDisponibles();
    }

    //Habitaciones libres por tipo
    @GetMapping("/habitaciones/disponibles/tipo/{tipo}")
    public List<Habitacion> getHabitacionesDisponiblesPorTipo(@PathVariable String tipo) {
        List<Habitacion> habitacionesDisponibles = habitacionService.getHabitacionesDisponibles();
        return habitacionesDisponibles.stream()
                .filter(h -> h.getTipo().equalsIgnoreCase(tipo))
                .collect(Collectors.toList());
    }

    //Habitaciones libres por precio max
    @GetMapping("/habitaciones/disponibles/precio/{precioMax}")
    public List<Habitacion> getHabitacionesDisponiblesPorPrecio(@PathVariable double precioMax) {
        List<Habitacion> habitacionesDisponibles = habitacionService.getHabitacionesDisponibles();
        return habitacionesDisponibles.stream()
                .filter(h -> h.getPrecioPorNoche() <= precioMax)
                .collect(Collectors.toList());
    }

    //Habitacion urgente
    @GetMapping("/habitaciones/urgente")
    public Habitacion getHabitacionUrgente() {
        return habitacionService.getHabitacionUrgente();
    }
    
    //Habitaciones urgentes
    @GetMapping("/habitaciones/urgentes")
    public List<Habitacion> getHabitacionesUrgentes() {
        return habitacionService.getHabitacionesUrgentes();
    }

    //Informe de ocupacion (Gerente)
    @GetMapping("/ocupacion")
    public Map<String, Object> obtenerOcupacionHotel() {
        return habitacionService.obtenerInformeOcupacion();
    }

}
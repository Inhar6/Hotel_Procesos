package com.example.restapi.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restapi.model.Habitacion;
import com.example.restapi.service.HabitacionService;

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
    // Método para obtener habitaciones que no están limpias
    @GetMapping("/habitaciones/noLimpias")
    public List<Habitacion> getHabitacionesNoLimpias() {
        return habitacionService.getHabitacionesNoLimpias();
    }

    @PutMapping("habitaciones/{id}/limpiar")
    public ResponseEntity<Habitacion> marcarHabitacionComoLimpia(@PathVariable Long id) {
        try {
            Habitacion habitacion = habitacionService.marcarHabitacionComoLimpia(id);
            return ResponseEntity.ok(habitacion);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("habitaciones/{id}/servicioLimpieza")
    public ResponseEntity<Habitacion> marcarHabitacionComoSucia(@PathVariable Long id) {
        try {
            Habitacion habitacion = habitacionService.marcarHabitacionComoSucia(id);
            return ResponseEntity.ok(habitacion);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Método para obtener una habitación que necesita limpieza urgente
    @GetMapping("/habitaciones/limpiezaUrgente")
    public Habitacion getHabitacionParaLimpiezaUrgente() {
        return habitacionService.getHabitacionParaLimpiezaUrgente();
    }

    //Informe de ocupacion (Gerente)
    @GetMapping("/ocupacion")
    public Map<String, Object> obtenerOcupacionHotel() {
        return habitacionService.obtenerInformeOcupacion();
    }

    //Sacar la habitacion por el id
    @GetMapping("/habitaciones/{id}")
    public ResponseEntity<Optional<Habitacion>> getHabitacionPorId(@PathVariable Long id) {
    try {
        Optional<Habitacion> habitacion = habitacionService.getHabitacionById(id);
        return ResponseEntity.ok(habitacion);
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    }


}
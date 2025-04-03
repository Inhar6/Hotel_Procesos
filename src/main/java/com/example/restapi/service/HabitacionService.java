package com.example.restapi.service;


import com.example.restapi.model.Habitacion;
import com.example.restapi.repository.HabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HabitacionService {

    private final HabitacionRepository habitacionRepository;

    @Autowired
    public HabitacionService(HabitacionRepository habitacionRepository) {
        this.habitacionRepository = habitacionRepository;
    }

    public List<Habitacion> getAllHabitaciones() {
        return habitacionRepository.findAll();
    }

    //Habitaciones libres
    public List<Habitacion> getHabitacionesDisponibles() {
        return habitacionRepository.findByDisponibleTrue();
    }

    public Optional<Habitacion> getHabitacionById(Long id) {
        return habitacionRepository.findById(id);
    }

    //Habitacion Urgente
    public List<Habitacion> getHabitacionesUrgentes() {
        return habitacionRepository.findByDisponibleTrueAndEstadoLimpiezaAndTieneProblemasFalse("Limpia");
    }
    
    public Habitacion getHabitacionUrgente() {
        List<Habitacion> habitacionesUrgentes = getHabitacionesUrgentes();
        if (habitacionesUrgentes.isEmpty()) {
            throw new RuntimeException("No hay habitaciones disponibles para asignación urgente");
        }
        return habitacionesUrgentes.get(0); // Devuelve la primera habitación disponible
    }

    // Método para obtener habitaciones que no están limpias
    public List<Habitacion> getHabitacionesNoLimpias() {
        return habitacionRepository.findByEstadoLimpiezaNot("Limpia");
    }
    
     // Método para obtener una habitación que necesita limpieza urgente
     public Habitacion getHabitacionParaLimpiezaUrgente() {
        List<Habitacion> habitacionesNoLimpias = getHabitacionesNoLimpias();
        if (habitacionesNoLimpias.isEmpty()) {
            throw new RuntimeException("No hay habitaciones que necesiten limpieza urgente");
        }
        return habitacionesNoLimpias.get(0); // Devuelve la primera habitación que necesita limpieza
    }

    //Informe de ocupacion (Gerente)
    public Map<String, Object> obtenerInformeOcupacion() {
        List<Habitacion> habitaciones = habitacionRepository.findAll();
        int totalHabitaciones = habitaciones.size();
        int ocupadas = (int) habitaciones.stream().filter(h -> !h.isDisponible()).count();
        int disponibles = totalHabitaciones - ocupadas;
        double porcentajeOcupacion = (totalHabitaciones == 0) ? 0 : (ocupadas * 100.0) / totalHabitaciones;

        Map<String, Object> informe = new HashMap<>();
        informe.put("totalHabitaciones", totalHabitaciones);
        informe.put("habitacionesOcupadas", ocupadas);
        informe.put("habitacionesDisponibles", disponibles);
        informe.put("porcentajeOcupacion", String.format("%.2f%%", porcentajeOcupacion));

        return informe;
    }

    /*
     * BD
     */
    public Habitacion createHabitacion(Habitacion habitacion) {
        return habitacionRepository.save(habitacion);
    }

    public Habitacion updateHabitacion(Long id, Habitacion habitacionDetails) {
        Optional<Habitacion> optionalHabitacion = habitacionRepository.findById(id);
        if (optionalHabitacion.isPresent()) {
            Habitacion habitacion = optionalHabitacion.get();
            habitacion.setNumero(habitacionDetails.getNumero());
            habitacion.setTipo(habitacionDetails.getTipo());
            habitacion.setPrecioPorNoche(habitacionDetails.getPrecioPorNoche());
            habitacion.setDisponible(habitacionDetails.isDisponible());
            return habitacionRepository.save(habitacion);
        } else {
            throw new RuntimeException("Habitación no encontrada");
        }
    }

    public void deleteHabitacion(Long id) {
        if (habitacionRepository.existsById(id)) {
            habitacionRepository.deleteById(id);
        } else {
            throw new RuntimeException("Habitación no encontrada con id: " + id);
        }
    }
}

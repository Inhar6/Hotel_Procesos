package com.example.restapi.service;


import com.example.restapi.model.Habitacion;
import com.example.restapi.repository.HabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
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

    public Optional<Habitacion> getHabitacionById(Long id) {
        return habitacionRepository.findById(id);
    }

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

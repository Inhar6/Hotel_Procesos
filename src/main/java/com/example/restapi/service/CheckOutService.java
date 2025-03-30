package com.example.restapi.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.restapi.model.Reserva;
import com.example.restapi.model.Habitacion;
import com.example.restapi.repository.ReservaRepository;
import com.example.restapi.repository.HabitacionRepository;

@Service
public class CheckOutService {

    private final ReservaRepository reservaRepository;
    private final HabitacionRepository habitacionRepository;

    @Autowired
    public CheckOutService(ReservaRepository reservaRepository, HabitacionRepository habitacionRepository) {
        this.reservaRepository = reservaRepository;
        this.habitacionRepository = habitacionRepository;
    }

    @Transactional
    public boolean realizarCheckOut(Long reservaId) {
        // Buscar la reserva por ID
        Optional<Reserva> reservaOpt = reservaRepository.findById(reservaId);
        if (reservaOpt.isEmpty()) {
            return false; // Reserva no encontrada
        }

        Reserva reserva = reservaOpt.get();

        // Verificar si el estado permite el check-out
        if (!"CheckIn Realizado".equals(reserva.getEstado())) {
            return false; // Solo se puede hacer check-out si está en "CheckIn Realizado"
        }

        // Actualizar el estado de la reserva
        reserva.setEstado("CheckOut Realizado");
        reservaRepository.save(reserva);

        // Actualizar la habitación asociada
        Habitacion habitacion = reserva.getHabitacion();
        habitacion.setDisponible(true);
        habitacion.setEstadoLimpieza("Urgente");
        habitacionRepository.save(habitacion);

        return true;
    }
}
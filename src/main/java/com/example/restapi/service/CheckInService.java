package com.example.restapi.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restapi.model.CheckIn;
import com.example.restapi.model.Reserva;
import com.example.restapi.model.Habitacion;
import com.example.restapi.repository.CheckInRepository;
import com.example.restapi.repository.ReservaRepository;
import com.example.restapi.repository.HabitacionRepository;

@Service
public class CheckInService {

    private final CheckInRepository checkInRepository;
    private final ReservaRepository reservaRepository;
    private final HabitacionRepository habitacionRepository;

    @Autowired
    public CheckInService(CheckInRepository checkInRepository, ReservaRepository reservaRepository, HabitacionRepository habitacionRepository) {
        this.checkInRepository = checkInRepository;
        this.reservaRepository = reservaRepository;
        this.habitacionRepository = habitacionRepository;
    }

    public boolean realizarCheckIn(CheckIn datosCheckIn) {
        // Buscar la reserva asociada
        Long reservaId = datosCheckIn.getReserva().getId();
        Optional<Reserva> reservaOpt = reservaRepository.findById(reservaId);

        if (reservaOpt.isPresent()) {
            Reserva reserva = reservaOpt.get();

            // Verificar si la reserva ya tiene un check-in o está cancelada
            if ("Cancelada".equals(reserva.getEstado())) {
                return false; // No se puede hacer check-in en una reserva cancelada
            }
            if (checkInRepository.findByReserva(reserva).size() > 0) {
                return false; // Ya existe un check-in para esta reserva
            }

            // Verificar que la habitación esté asignada y no disponible
            Habitacion habitacion = reserva.getHabitacion();
            if (habitacion.isDisponible()) {
                // Si la habitación está disponible, marcarla como ocupada
                habitacion.setDisponible(false);
                habitacionRepository.save(habitacion);
            }

            // Actualizar el estado de la reserva a "Confirmada" o similar
            reserva.setEstado("CheckIn Realizado"); // Puedes ajustar este estado según tu lógica
            reserva.setFechaCheckIn(datosCheckIn.getFechaCheckIn()); // Actualizar fechas si cambian
            reserva.setFechaCheckOut(datosCheckIn.getFechaCheckOut());
            reserva.setMetodoPago(datosCheckIn.getMetodoPago());
            reservaRepository.save(reserva);

            // Guardar el check-in
            datosCheckIn.setReserva(reserva); // Asegurar que la relación esté bien establecida
            checkInRepository.save(datosCheckIn);

            return true;
        }
        return false; // Reserva no encontrada
    }
}
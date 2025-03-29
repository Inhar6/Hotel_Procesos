package com.example.restapi.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.Habitacion;
import com.example.restapi.model.Reserva;
import com.example.restapi.repository.ClienteRepository;
import com.example.restapi.repository.HabitacionRepository;
import com.example.restapi.repository.ReservaRepository;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final HabitacionRepository habitacionRepository;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository, ClienteRepository clienteRepository, HabitacionRepository habitacionRepository) {
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
        this.habitacionRepository = habitacionRepository;
    }

    public boolean reservarHabitacion(Long clienteId, Long habitacionId, LocalDate fechaCheckIn, LocalDate fechaCheckOut, String metodoPago) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(clienteId);
        Optional<Habitacion> habitacionOpt = habitacionRepository.findById(habitacionId);

        if (clienteOpt.isPresent() && habitacionOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            Habitacion habitacion = habitacionOpt.get();

            // Verificar si la habitación ya está ocupada
            if (!habitacion.isDisponible()) {
                return false;
            }

            // Crear la reserva
            Reserva reserva = new Reserva(cliente, habitacion, fechaCheckIn, fechaCheckOut, metodoPago);
            reservaRepository.save(reserva);

            // Marcar la habitación como ocupada
            habitacion.setDisponible(false);
            habitacionRepository.save(habitacion);

            return true;
        }
        return false;
    }

    public boolean cancelarReserva(Long reservaId) {
        Optional<Reserva> reservaOpt = reservaRepository.findById(reservaId);

        if (reservaOpt.isPresent()) {
            Reserva reserva = reservaOpt.get();
            reserva.cancelarReserva();
            reservaRepository.save(reserva);

            // Marcar la habitación como disponible nuevamente
            Habitacion habitacion = reserva.getHabitacion();
            habitacion.setDisponible(true);
            habitacionRepository.save(habitacion);

            return true;
        }
        return false;
    }

    public List<Reserva> getAllReservas() {
        return reservaRepository.findAll();
    }
}

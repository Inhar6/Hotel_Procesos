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

            if (!habitacion.isDisponible()) {
                return false;
            }

            Reserva reserva = new Reserva(cliente, habitacion, fechaCheckIn, fechaCheckOut, metodoPago);
            reservaRepository.save(reserva);

            habitacion.setDisponible(false);
            habitacionRepository.save(habitacion);

            return true;
        }
        return false;
    }

    public boolean modificarReserva(Long reservaId, Long habitacionId, LocalDate fechaCheckIn, LocalDate fechaCheckOut, String metodoPago, Double totalPagar) {
        Optional<Reserva> reservaOpt = reservaRepository.findById(reservaId);
        Optional<Habitacion> habitacionOpt = habitacionRepository.findById(habitacionId);

        if (reservaOpt.isPresent() && habitacionOpt.isPresent()) {
            Reserva reserva = reservaOpt.get();
            Habitacion nuevaHabitacion = habitacionOpt.get();

            // Validar que la reserva esté en estado "Pendiente"
            if (!reserva.getEstado().equalsIgnoreCase("Pendiente")) {
                return false;
            }

            // Verificar que la nueva habitación esté disponible (si es diferente a la actual)
            if (!nuevaHabitacion.getId().equals(reserva.getHabitacion().getId()) && !nuevaHabitacion.isDisponible()) {
                return false; // La nueva habitación no está disponible
            }

            // Si la habitación cambia, liberar la anterior
            if (!nuevaHabitacion.getId().equals(reserva.getHabitacion().getId())) {
                Habitacion habitacionAnterior = reserva.getHabitacion();
                habitacionAnterior.setDisponible(true);
                habitacionRepository.save(habitacionAnterior);

                reserva.setHabitacion(nuevaHabitacion);
                nuevaHabitacion.setDisponible(false);
                habitacionRepository.save(nuevaHabitacion);
            }

            // Actualizar los campos
            reserva.setFechaCheckIn(fechaCheckIn);
            reserva.setFechaCheckOut(fechaCheckOut);
            reserva.setMetodoPago(metodoPago);
            reserva.setTotalPagar(totalPagar); // Usar el totalPagar enviado por el frontend

            // Guardar los cambios en la reserva
            reservaRepository.save(reserva);
            return true;
        }
        return false; // Reserva o habitación no encontrada
    }

    public boolean cancelarReserva(Long reservaId) {
        Optional<Reserva> reservaOpt = reservaRepository.findById(reservaId);

        if (reservaOpt.isPresent()) {
            Reserva reserva = reservaOpt.get();
            if ("Cancelada".equals(reserva.getEstado())) {
                return false;
            }
            reserva.cancelarReserva();
            reservaRepository.save(reserva);

            Habitacion habitacion = reserva.getHabitacion();
            habitacion.setDisponible(true);
            habitacionRepository.save(habitacion);

            return true;
        }
        return false;
    }

    public List<Reserva> getReservasPorEmail(String email) {
        return reservaRepository.findByClienteEmail(email);
    }

    public Optional<Reserva> getReservaById(Long id) {
        return reservaRepository.findById(id);
    }

    public List<Reserva> getAllReservas() {
        return reservaRepository.findAll();
    }

    public List<Reserva> obtenerReservasPendientes() {
        return reservaRepository.findByEstado("Pendiente");
    }
}
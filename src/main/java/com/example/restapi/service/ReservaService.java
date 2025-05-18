package com.example.restapi.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.Habitacion;
import com.example.restapi.model.Reserva;
import com.example.restapi.repository.ClienteRepository;
import com.example.restapi.repository.HabitacionRepository;
import com.example.restapi.repository.ReservaRepository;

@Service
public class ReservaService {

    private static final Logger logger = LoggerFactory.getLogger(ReservaService.class);

    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final HabitacionRepository habitacionRepository;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository, ClienteRepository clienteRepository, HabitacionRepository habitacionRepository) {
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
        this.habitacionRepository = habitacionRepository;
    }

    @Transactional
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

    @Transactional
    public boolean modificarReserva(Long reservaId, Long habitacionId, LocalDate fechaCheckIn, LocalDate fechaCheckOut, String metodoPago, Double totalPagar) {
        logger.info("Modificando reserva ID: {}, habitacionId: {}, fechaCheckIn: {}, fechaCheckOut: {}, metodoPago: {}, totalPagar: {}",
                reservaId, habitacionId, fechaCheckIn, fechaCheckOut, metodoPago, totalPagar);

        Optional<Reserva> reservaOpt = reservaRepository.findById(reservaId);
        Optional<Habitacion> habitacionOpt = habitacionRepository.findById(habitacionId);

        if (!reservaOpt.isPresent()) {
            logger.error("Reserva no encontrada para ID: {}", reservaId);
            return false;
        }
        if (!habitacionOpt.isPresent()) {
            logger.error("Habitación no encontrada para ID: {}", habitacionId);
            return false;
        }

        Reserva reserva = reservaOpt.get();
        Habitacion nuevaHabitacion = habitacionOpt.get();

        // Validar estado: solo "Pendiente" o "CheckIn Realizado"
        if (!reserva.getEstado().equalsIgnoreCase("Pendiente") && !reserva.getEstado().equalsIgnoreCase("CheckIn Realizado")) {
            logger.error("Estado inválido para modificar reserva ID: {}. Estado actual: {}", reservaId, reserva.getEstado());
            return false;
        }

        // Si no se cambia la habitación, verificar que esté ocupada (disponible = false)
        if (nuevaHabitacion.getId().equals(reserva.getHabitacion().getId())) {
            if (nuevaHabitacion.isDisponible()) {
                logger.error("La habitación ID: {} está libre, pero debería estar ocupada para la reserva ID: {}", habitacionId, reservaId);
                return false;
            }
        } else {
            // Si se cambia la habitación, verificar que la nueva esté disponible
            if (!nuevaHabitacion.isDisponible()) {
                logger.error("La nueva habitación ID: {} no está disponible", habitacionId);
                return false;
            }

            // Liberar la habitación anterior
            Habitacion habitacionAnterior = reserva.getHabitacion();
            habitacionAnterior.setDisponible(true);
            habitacionRepository.save(habitacionAnterior);

            // Ocupar la nueva habitación
            nuevaHabitacion.setDisponible(false);
            habitacionRepository.save(nuevaHabitacion);
            reserva.setHabitacion(nuevaHabitacion);
        }

        // Actualizar los campos
        reserva.setFechaCheckIn(fechaCheckIn);
        reserva.setFechaCheckOut(fechaCheckOut);
        reserva.setMetodoPago(metodoPago);
        reserva.setTotalPagar(totalPagar);

        // Guardar los cambios
        logger.info("Guardando reserva actualizada ID: {}", reservaId);
        reservaRepository.save(reserva);
        return true;
    }

    @Transactional
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
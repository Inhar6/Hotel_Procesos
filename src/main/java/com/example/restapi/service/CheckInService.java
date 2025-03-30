package com.example.restapi.service;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.restapi.model.CheckIn;
import com.example.restapi.model.Reserva;
import com.example.restapi.model.Habitacion;
import com.example.restapi.model.Cliente;
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
        if (datosCheckIn == null || datosCheckIn.getReserva() == null || datosCheckIn.getReserva().getId() == null) {
            return false;
        }

        Long reservaId = datosCheckIn.getReserva().getId();
        Optional<Reserva> reservaOpt = reservaRepository.findById(reservaId);

        if (reservaOpt.isPresent()) {
            Reserva reserva = reservaOpt.get();

            Cliente clienteReserva = reserva.getCliente();
            if (clienteReserva == null || 
                !clienteReserva.getNombre().equals(datosCheckIn.getNombreHuesped()) ||
                !clienteReserva.getApellido().equals(datosCheckIn.getApellidosHuesped())) {
                return false;
            }

            if ("Cancelada".equals(reserva.getEstado()) || checkInRepository.findByReserva(reserva).size() > 0) {
                return false;
            }

            Habitacion habitacion = reserva.getHabitacion();
            if (habitacion.isDisponible()) {
                habitacion.setDisponible(false);
                habitacionRepository.save(habitacion);
            }

            reserva.setEstado("CheckIn Realizado");
            reserva.setFechaCheckIn(datosCheckIn.getFechaCheckIn());
            reserva.setFechaCheckOut(datosCheckIn.getFechaCheckOut());
            reserva.setMetodoPago(datosCheckIn.getMetodoPago());
            reservaRepository.save(reserva);

            datosCheckIn.setReserva(reserva);
            checkInRepository.save(datosCheckIn);

            return true;
        }
        return false;
    }
}
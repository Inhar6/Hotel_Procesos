package com.example.restapi.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.restapi.model.CheckIn;
import com.example.restapi.model.Reserva;
import com.example.restapi.repository.CheckInRepository;
import com.example.restapi.repository.ReservaRepository;

@Service
public class CheckInService {

    private final CheckInRepository checkInRepository;
    private final ReservaRepository reservaRepository;

    @Autowired
    public CheckInService(CheckInRepository checkInRepository, ReservaRepository reservaRepository) {
        this.checkInRepository = checkInRepository;
        this.reservaRepository = reservaRepository;
    }

    public boolean realizarCheckIn(CheckIn datosCheckIn) {
        Optional<Reserva> reservaOpt = reservaRepository.findById(datosCheckIn.getReservaId());
        if (reservaOpt.isEmpty()) {
            return false;
        }

        Reserva reserva = reservaOpt.get();
        if ("CheckIn Realizado".equals(reserva.getEstado())) {
            return false;
        }

        reserva.setEstado("CheckIn Realizado");
        reservaRepository.save(reserva);
        checkInRepository.save(datosCheckIn);

        return true;
    }
}
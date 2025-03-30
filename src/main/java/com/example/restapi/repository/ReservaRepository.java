package com.example.restapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.Habitacion;
import com.example.restapi.model.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Buscar todas las reservas de un cliente específico
    List<Reserva> findByCliente(Cliente cliente);
    // Buscar todas las reservas de una habitación
    List<Reserva> findByHabitacion(Habitacion habitacion);
    // Buscar reservas por estado
    List<Reserva> findByEstado(String estado);
    // Nuevo método para buscar reservas por email de cliente
    List<Reserva> findByClienteEmail(String email); 
}

package com.example.restapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.restapi.model.Habitacion;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

    // Método para obtener habitaciones disponibles
    List<Habitacion> findByDisponibleTrue();
    // Metodo para habitacionUrgente
    List<Habitacion> findByDisponibleTrueAndEstadoLimpiezaAndTieneProblemasFalse(String estadoLimpieza);

    // Otros métodos personalizados si es necesario
}

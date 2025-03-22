package com.example.restapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.restapi.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Puedes agregar consultas personalizadas si las necesitas
    Optional<Cliente> findByEmail(String email);

    // Otros métodos si es necesario
}

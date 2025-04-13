package com.example.restapi.integracion.repository;

import com.example.restapi.model.Cliente;
import com.example.restapi.repository.ClienteRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class ClienteRepositoryIntegrationTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    public void testFindByEmail() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan");
        cliente.setApellido("Pérez");
        cliente.setEmail("juan.perez@example.com");
        cliente.setContraseña("password123");
        cliente.setMetodoPago("PayPal");
        cliente.setTelefono("941157787");
        clienteRepository.save(cliente);

        // Act
        Optional<Cliente> foundCliente = clienteRepository.findByEmail("juan.perez@example.com");

        // Assert
        assertTrue(foundCliente.isPresent());
        assertEquals("Juan", foundCliente.get().getNombre());
        assertEquals("Pérez", foundCliente.get().getApellido());
        assertEquals("juan.perez@example.com", foundCliente.get().getEmail());
    }

    @Test
    public void testFindByEmailNotFound() {
        // Act
        Optional<Cliente> foundCliente = clienteRepository.findByEmail("nonexistent@example.com");

        // Assert
        assertFalse(foundCliente.isPresent());
    }
}
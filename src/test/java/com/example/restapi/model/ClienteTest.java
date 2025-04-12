package com.example.restapi.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClienteTest {

    @Test
    public void testClienteConstructorAndGetters() {
        Cliente cliente = new Cliente("Juan", "Pérez", "contraseña123", "juan@example.com", "Tarjeta", "123456789");

        assertEquals("Juan", cliente.getNombre());
        assertEquals("Pérez", cliente.getApellido());
        assertEquals("contraseña123", cliente.getContraseña());
        assertEquals("juan@example.com", cliente.getEmail());
        assertEquals("Tarjeta", cliente.getMetodoPago());
        assertEquals("123456789", cliente.getTelefono());
    }

    @Test
    public void testSetters() {
        Cliente cliente = new Cliente();
        cliente.setNombre("María");
        cliente.setApellido("Gómez");
        cliente.setContraseña("nuevaContraseña");
        cliente.setEmail("maria@example.com");
        cliente.setMetodoPago("PayPal");
        cliente.setTelefono("987654321");

        assertEquals("María", cliente.getNombre());
        assertEquals("Gómez", cliente.getApellido());
        assertEquals("nuevaContraseña", cliente.getContraseña());
        assertEquals("maria@example.com", cliente.getEmail());
        assertEquals("PayPal", cliente.getMetodoPago());
        assertEquals("987654321", cliente.getTelefono());
    }
    @Test
    public void testGetId() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        assertEquals(1L, cliente.getId());
    }

    @Test
    public void testSetId() {
        Cliente cliente = new Cliente();
        cliente.setId(2L);
        assertEquals(2L, cliente.getId());
    }
}
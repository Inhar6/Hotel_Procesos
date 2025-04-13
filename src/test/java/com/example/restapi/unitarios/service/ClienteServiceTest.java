package com.example.restapi.unitarios.service;

import com.example.restapi.model.Cliente;
import com.example.restapi.repository.ClienteRepository;
import com.example.restapi.service.ClienteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    public void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan");
        cliente.setApellido("Perez");
        cliente.setEmail("juan.perez@example.com");
        cliente.setContraseña("password123");
    }

    @Test
    public void testGetAllClientes() {
        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente));
        List<Cliente> clientes = clienteService.getAllClientes();
        assertEquals(1, clientes.size());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    public void testGetClienteById() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        Optional<Cliente> foundCliente = clienteService.getClienteById(1L);
        assertTrue(foundCliente.isPresent());
        assertEquals("Juan", foundCliente.get().getNombre());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetClienteByEmail() {
        when(clienteRepository.findByEmail("juan.perez@example.com")).thenReturn(Optional.of(cliente));
        Optional<Cliente> foundCliente = clienteService.getClienteByEmail("juan.perez@example.com");
        assertTrue(foundCliente.isPresent());
        assertEquals("Juan", foundCliente.get().getNombre());
        verify(clienteRepository, times(1)).findByEmail("juan.perez@example.com");
    }

    @Test
    public void testVerificarCredenciales() {
        when(clienteRepository.findByEmail("juan.perez@example.com")).thenReturn(Optional.of(cliente));
        boolean validCredentials = clienteService.verificarCredenciales("juan.perez@example.com", "password123");
        assertTrue(validCredentials);
        verify(clienteRepository, times(1)).findByEmail("juan.perez@example.com");
    }

    @Test
    public void testCreateCliente() {
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        Cliente savedCliente = clienteService.createCliente(cliente);
        assertNotNull(savedCliente);
        assertEquals("Juan", savedCliente.getNombre());
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    public void testUpdateCliente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        Cliente updatedCliente = clienteService.updateCliente(1L, cliente);
        assertNotNull(updatedCliente);
        assertEquals("Juan", updatedCliente.getNombre());
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    public void testDeleteCliente() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(clienteRepository).deleteById(1L);
        clienteService.deleteCliente(1L);
        verify(clienteRepository, times(1)).existsById(1L);
        verify(clienteRepository, times(1)).deleteById(1L);
    }
    @Test
    public void testClienteGetId() {
        assertEquals(1L, cliente.getId());
    }

    @Test
    public void testClienteSetId() {
        cliente.setId(2L);
        assertEquals(2L, cliente.getId());
    }
}
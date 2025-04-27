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
import java.util.Collections;
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
        cliente.setTelefono("123456789");
    }

    @Test
    public void testGetAllClientes() {
        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente));
        List<Cliente> clientes = clienteService.getAllClientes();
        assertEquals(1, clientes.size());
        verify(clienteRepository, times(1)).findAll();
    }
    
    @Test
    public void testGetAllClientesEmpty() {
        when(clienteRepository.findAll()).thenReturn(Collections.emptyList());
        List<Cliente> clientes = clienteService.getAllClientes();
        assertTrue(clientes.isEmpty());
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
    public void testGetClienteByIdNotFound() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Cliente> foundCliente = clienteService.getClienteById(99L);
        assertFalse(foundCliente.isPresent());
        verify(clienteRepository, times(1)).findById(99L);
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
    public void testGetClienteByEmailNotFound() {
        when(clienteRepository.findByEmail("noexiste@example.com")).thenReturn(Optional.empty());
        Optional<Cliente> foundCliente = clienteService.getClienteByEmail("noexiste@example.com");
        assertFalse(foundCliente.isPresent());
        verify(clienteRepository, times(1)).findByEmail("noexiste@example.com");
    }

    @Test
    public void testVerificarCredencialesValid() {
        when(clienteRepository.findByEmail("juan.perez@example.com")).thenReturn(Optional.of(cliente));
        boolean validCredentials = clienteService.verificarCredenciales("juan.perez@example.com", "password123");
        assertTrue(validCredentials);
        verify(clienteRepository, times(1)).findByEmail("juan.perez@example.com");
    }
    
    @Test
    public void testVerificarCredencialesInvalidPassword() {
        when(clienteRepository.findByEmail("juan.perez@example.com")).thenReturn(Optional.of(cliente));
        boolean validCredentials = clienteService.verificarCredenciales("juan.perez@example.com", "wrongpassword");
        assertFalse(validCredentials);
        verify(clienteRepository, times(1)).findByEmail("juan.perez@example.com");
    }
    
    @Test
    public void testVerificarCredencialesInvalidEmail() {
        when(clienteRepository.findByEmail("noexiste@example.com")).thenReturn(Optional.empty());
        boolean validCredentials = clienteService.verificarCredenciales("noexiste@example.com", "password123");
        assertFalse(validCredentials);
        verify(clienteRepository, times(1)).findByEmail("noexiste@example.com");
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
        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setId(1L);
        clienteActualizado.setNombre("JuanActualizado");
        clienteActualizado.setApellido("PerezActualizado");
        clienteActualizado.setEmail("juan.actualizado@example.com");
        clienteActualizado.setTelefono("987654321");
        
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        Cliente updatedCliente = clienteService.updateCliente(1L, clienteActualizado);
        
        assertNotNull(updatedCliente);
        assertEquals("JuanActualizado", updatedCliente.getNombre());
        assertEquals("PerezActualizado", updatedCliente.getApellido());
        assertEquals("juan.actualizado@example.com", updatedCliente.getEmail());
        assertEquals("987654321", updatedCliente.getTelefono());
        
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(cliente);
    }
    
    @Test
    public void testUpdateClienteNotFound() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            clienteService.updateCliente(99L, cliente);
        });
        
        assertEquals("Cliente no encontrado", exception.getMessage());
        verify(clienteRepository, times(1)).findById(99L);
        verify(clienteRepository, never()).save(any(Cliente.class));
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
    public void testDeleteClienteNotFound() {
        when(clienteRepository.existsById(99L)).thenReturn(false);
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            clienteService.deleteCliente(99L);
        });
        
        assertEquals("Cliente no encontrado con id: 99", exception.getMessage());
        verify(clienteRepository, times(1)).existsById(99L);
        verify(clienteRepository, never()).deleteById(any());
    }

    // Nota: Estos tests están realmente probando la clase Cliente, no ClienteService
    // Podrían moverse a una clase ClienteTest separada
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
package com.example.restapi.unitarios.controller;

import com.example.restapi.controller.ClienteController;
import com.example.restapi.model.Cliente;
import com.example.restapi.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    private Cliente cliente;

    @BeforeEach
    public void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setEmail("juan@example.com");
        cliente.setContraseña("password123");
    }

    @Test
    public void testGetAllClientes() {
        // Arrange
        List<Cliente> clientes = Arrays.asList(cliente);
        when(clienteService.getAllClientes()).thenReturn(clientes);

        // Act
        ResponseEntity<List<Cliente>> response = clienteController.getAllClientes();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clientes, response.getBody());
        verify(clienteService, times(1)).getAllClientes();
    }

    @Test
    public void testGetClienteById_ClienteExiste() {
        // Arrange
        when(clienteService.getClienteById(1L)).thenReturn(Optional.of(cliente));

        // Act
        ResponseEntity<Cliente> response = clienteController.getClienteById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cliente, response.getBody());
        verify(clienteService, times(1)).getClienteById(1L);
    }

    @Test
    public void testGetClienteById_ClienteNoExiste() {
        // Arrange
        when(clienteService.getClienteById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Cliente> response = clienteController.getClienteById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(clienteService, times(1)).getClienteById(1L);
    }

    @Test
    public void testLogin_CredencialesCorrectas() {
        // Arrange
        Map<String, String> credenciales = new HashMap<>();
        credenciales.put("email", "juan@example.com");
        credenciales.put("contraseña", "password123");
        when(clienteService.getClienteByEmail("juan@example.com")).thenReturn(Optional.of(cliente));

        // Act
        ResponseEntity<?> response = clienteController.login(credenciales);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> datosCliente = (Map<String, Object>) response.getBody();
        assertEquals(cliente.getId(), datosCliente.get("id"));
        assertEquals(cliente.getEmail(), datosCliente.get("email"));
        assertEquals(cliente.getNombre(), datosCliente.get("nombre"));
        verify(clienteService, times(1)).getClienteByEmail("juan@example.com");
    }

    @Test
    public void testLogin_CredencialesInvalidas() {
        // Arrange
        Map<String, String> credenciales = new HashMap<>();
        credenciales.put("email", "juan@example.com");
        credenciales.put("contraseña", "wrongpassword");
        when(clienteService.getClienteByEmail("juan@example.com")).thenReturn(Optional.of(cliente));

        // Act
        ResponseEntity<?> response = clienteController.login(credenciales);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Credenciales incorrectas.", response.getBody());
        verify(clienteService, times(1)).getClienteByEmail("juan@example.com");
    }

    @Test
    public void testLogin_CamposFaltantes() {
        // Arrange
        Map<String, String> credenciales = new HashMap<>();
        credenciales.put("email", "juan@example.com");

        // Act
        ResponseEntity<?> response = clienteController.login(credenciales);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email y contraseña son requeridos.", response.getBody());
        verifyNoInteractions(clienteService);
    }

    @Test
    public void testRegistrarCliente_EmailNoRegistrado() {
        // Arrange
        when(clienteService.getClienteByEmail(cliente.getEmail())).thenReturn(Optional.empty());
        when(clienteService.createCliente(any(Cliente.class))).thenReturn(cliente);

        // Act
        ResponseEntity<String> response = clienteController.registrarCliente(cliente);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Registro exitoso", response.getBody());
        verify(clienteService, times(1)).getClienteByEmail(cliente.getEmail());
        verify(clienteService, times(1)).createCliente(any(Cliente.class));
    }

    @Test
    public void testRegistrarCliente_EmailYaRegistrado() {
        // Arrange
        when(clienteService.getClienteByEmail(cliente.getEmail())).thenReturn(Optional.of(cliente));

        // Act
        ResponseEntity<String> response = clienteController.registrarCliente(cliente);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El correo ya está registrado.", response.getBody());
        verify(clienteService, times(1)).getClienteByEmail(cliente.getEmail());
        verify(clienteService, never()).createCliente(any(Cliente.class));
    }

    @Test
    public void testCreateCliente() {
        // Arrange
        when(clienteService.createCliente(any(Cliente.class))).thenReturn(cliente);

        // Act
        ResponseEntity<Cliente> response = clienteController.createCliente(cliente);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cliente, response.getBody());
        verify(clienteService, times(1)).createCliente(any(Cliente.class));
    }

    @Test
    public void testUpdateCliente_ClienteExiste() {
        // Arrange
        when(clienteService.updateCliente(anyLong(), any(Cliente.class))).thenReturn(cliente);

        // Act
        ResponseEntity<Cliente> response = clienteController.updateCliente(1L, cliente);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cliente, response.getBody());
        verify(clienteService, times(1)).updateCliente(1L, cliente);
    }

    @Test
    public void testUpdateCliente_ClienteNoExiste() {
        // Arrange
        when(clienteService.updateCliente(anyLong(), any(Cliente.class)))
                .thenThrow(new RuntimeException("Cliente no encontrado"));

        // Act
        ResponseEntity<Cliente> response = clienteController.updateCliente(1L, cliente);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(clienteService, times(1)).updateCliente(1L, cliente);
    }

    @Test
    public void testDeleteCliente_ClienteExiste() {
        // Arrange
        doNothing().when(clienteService).deleteCliente(anyLong());

        // Act
        ResponseEntity<Void> response = clienteController.deleteCliente(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clienteService, times(1)).deleteCliente(1L);
    }

    @Test
    public void testDeleteCliente_ClienteNoExiste() {
        // Arrange
        doThrow(new RuntimeException("Cliente no encontrado")).when(clienteService).deleteCliente(anyLong());

        // Act
        ResponseEntity<Void> response = clienteController.deleteCliente(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(clienteService, times(1)).deleteCliente(1L);
    }
}
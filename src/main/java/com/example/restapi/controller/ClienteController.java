package com.example.restapi.controller;

import com.example.restapi.model.Cliente;
import com.example.restapi.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Obtener todos los clientes
    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> getAllClientes() {
        List<Cliente> clientes = clienteService.getAllClientes();
        return ResponseEntity.ok(clientes);
    }

    // Obtener un cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.getClienteById(id);
        return cliente.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Inicio de sesion
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String email = credenciales.get("email");
        String contraseña = credenciales.get("contraseña");
    
        if (email == null || contraseña == null) {
            return ResponseEntity.badRequest().body("Email y contraseña son requeridos.");
        }
    
        Optional<Cliente> clienteOptional = clienteService.getClienteByEmail(email);
    
        // Verificamos que exista y que la contraseña coincida
        if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();
    
            if (cliente.getContraseña().equals(contraseña)) {
                Map<String, Object> datosCliente = new HashMap<>();
                datosCliente.put("id", cliente.getId());
                datosCliente.put("email", cliente.getEmail());
                datosCliente.put("nombre", cliente.getNombre());
    
                return ResponseEntity.ok(datosCliente);
            }
        }
    
        return ResponseEntity.status(401).body("Credenciales incorrectas.");
    }

    
    // Método para registrar un nuevo cliente
    @PostMapping("/registro")
    public ResponseEntity<String> registrarCliente(@RequestBody Cliente cliente) {
        // Verificar si el email ya está registrado
        Optional<Cliente> clienteExistente = clienteService.getClienteByEmail(cliente.getEmail());
        if (clienteExistente.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El correo ya está registrado.");
        }
        // Guardar nuevo cliente
        Cliente nuevoCliente = clienteService.createCliente(cliente);
        System.out.println(nuevoCliente);
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro exitoso");
    }

    // Registrar un nuevo cliente
    @PostMapping
    public ResponseEntity<Cliente> createCliente(@RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.createCliente(cliente);
        return ResponseEntity.ok(nuevoCliente);
    }

    // Actualizar datos de un cliente
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @RequestBody Cliente clienteDetails) {
        try {
            Cliente clienteActualizado = clienteService.updateCliente(id, clienteDetails);
            return ResponseEntity.ok(clienteActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un cliente por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        try {
            clienteService.deleteCliente(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

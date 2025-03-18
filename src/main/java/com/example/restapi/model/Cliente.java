package com.example.restapi.model;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private Long id;               // Identificador único del cliente
    private String nombre;         // Nombre del cliente
    private String apellido;       // Apellido del cliente
    private String email;          // Correo electrónico para reservas y notificaciones
    private String telefono;       // Número de contacto del cliente
    private String metodoPago;     // Método de pago preferido (tarjeta, PayPal, etc.)
    private List<Reserva> reservas; // Lista de reservas asociadas al cliente

    // Constructor
    public Cliente(Long id, String nombre, String apellido, String email, String telefono, String metodoPago) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.metodoPago = metodoPago;
        this.reservas = new ArrayList<>();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public List<Reserva> getReservas() { return reservas; }
    public void addReserva(Reserva reserva) { this.reservas.add(reserva); }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", metodoPago='" + metodoPago + '\'' +
                '}';
    }
    
}


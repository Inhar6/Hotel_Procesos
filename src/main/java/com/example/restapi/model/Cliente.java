package com.example.restapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String apellido;

    @Column(nullable = false, length = 255)
    private String contraseña;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 50)
    private String metodoPago;

    @Column(nullable = false, length = 15)
    private String telefono;

    // Constructores
    public Cliente() {}

    public Cliente(String nombre, String apellido, String contraseña, String email, String metodoPago, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.contraseña = contraseña;
        this.email = email;
        this.metodoPago = metodoPago;
        this.telefono = telefono;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
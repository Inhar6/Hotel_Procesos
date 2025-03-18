package com.example.restapi.model;

import java.util.ArrayList;
import java.util.List;

public class Habitacion {
    private Long id;                // Identificador único de la habitación
    private int numero;             // Número de la habitación
    private String tipo;            // Tipo de habitación (Ej: "Individual", "Doble", "Suite")
    private double precioPorNoche;  // Precio por noche de la habitación
    private boolean disponible;     // Indica si la habitación está disponible o no
    private String estadoLimpieza;  // Estado de limpieza ("Limpia", "Sucia", "En mantenimiento")
    private boolean tieneProblemas; // Indica si tiene algún problema reportado
    private List<Reserva> reservas; // Lista de reservas asociadas a la habitación

    // Constructor
    public Habitacion(Long id, int numero, String tipo, double precioPorNoche) {
        this.id = id;
        this.numero = numero;
        this.tipo = tipo;
        this.precioPorNoche = precioPorNoche;
        this.disponible = true; // Por defecto, la habitación está disponible
        this.estadoLimpieza = "Limpia"; // Por defecto, la habitación está limpia
        this.tieneProblemas = false;
        this.reservas = new ArrayList<>();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getPrecioPorNoche() { return precioPorNoche; }
    public void setPrecioPorNoche(double precioPorNoche) { this.precioPorNoche = precioPorNoche; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public String getEstadoLimpieza() { return estadoLimpieza; }
    public void setEstadoLimpieza(String estadoLimpieza) { this.estadoLimpieza = estadoLimpieza; }

    public boolean isTieneProblemas() { return tieneProblemas; }
    public void setTieneProblemas(boolean tieneProblemas) { this.tieneProblemas = tieneProblemas; }

    public List<Reserva> getReservas() { return reservas; }
    public void addReserva(Reserva reserva) { this.reservas.add(reserva); }

    @Override
    public String toString() {
        return "Habitacion{" +
                "id=" + id +
                ", numero=" + numero +
                ", tipo='" + tipo + '\'' +
                ", precioPorNoche=" + precioPorNoche +
                ", disponible=" + disponible +
                ", estadoLimpieza='" + estadoLimpieza + '\'' +
                ", tieneProblemas=" + tieneProblemas +
                '}';
    }
}


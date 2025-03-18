package com.example.restapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "habitaciones")
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private int numero;

    @Column(nullable = false, length = 20)
    private String tipo;

    @Column(nullable = false)
    private double precioPorNoche;

    @Column(nullable = false)
    private boolean disponible = true;

    @Column(nullable = false, length = 20)
    private String estadoLimpieza = "Limpia";

    @Column(nullable = false)
    private boolean tieneProblemas = false;

    public Habitacion() {}

    public Habitacion(int numero, String tipo, double precioPorNoche) {
        this.numero = numero;
        this.tipo = tipo;
        this.precioPorNoche = precioPorNoche;
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


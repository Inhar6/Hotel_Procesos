package com.example.restapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "habitaciones")
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int numero;

    @Column(name = "precio_por_noche", nullable = false)
    private double precioPorNoche;

    @Column(nullable = false)
    private boolean disponible;

    @Column(name = "estado_limpieza", nullable = false, length = 20)
    private String estadoLimpieza;

    @Column(name = "tiene_problemas", nullable = false)
    private boolean tieneProblemas;

    @Column(nullable = false, length = 20)
    private String tipo;

    // Constructores
    public Habitacion() {}

    public Habitacion(int numero, double precioPorNoche, boolean disponible, String estadoLimpieza, boolean tieneProblemas, String tipo) {
        this.numero = numero;
        this.precioPorNoche = precioPorNoche;
        this.disponible = disponible;
        this.estadoLimpieza = estadoLimpieza;
        this.tieneProblemas = tieneProblemas;
        this.tipo = tipo;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
    public double getPrecioPorNoche() { return precioPorNoche; }
    public void setPrecioPorNoche(double precioPorNoche) { this.precioPorNoche = precioPorNoche; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    public String getEstadoLimpieza() { return estadoLimpieza; }
    public void setEstadoLimpieza(String estadoLimpieza) { this.estadoLimpieza = estadoLimpieza; }
    public boolean isTieneProblemas() { return tieneProblemas; }
    public void setTieneProblemas(boolean tieneProblemas) { this.tieneProblemas = tieneProblemas; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
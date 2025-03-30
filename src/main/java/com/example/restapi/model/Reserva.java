package com.example.restapi.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER) // Cambia a EAGER para cargar siempre el cliente
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonProperty("clienteId") // Serializa solo el ID como "clienteId"
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "habitacion_id", nullable = false)
    private Habitacion habitacion;

    @Column(nullable = false)
    private LocalDate fechaCheckIn;

    @Column(nullable = false)
    private LocalDate fechaCheckOut;

    @Column(nullable = false)
    private double totalPagar;

    @Column(nullable = false, length = 20)
    private String estado = "Pendiente";

    @Column(nullable = false, length = 50)
    private String metodoPago;

    // Constructores
    public Reserva() {}

    public Reserva(Cliente cliente, Habitacion habitacion, LocalDate fechaCheckIn, LocalDate fechaCheckOut, String metodoPago) {
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.fechaCheckIn = fechaCheckIn;
        this.fechaCheckOut = fechaCheckOut;
        this.metodoPago = metodoPago;
        this.totalPagar = calcularTotal();
    }

    private double calcularTotal() {
        long noches = fechaCheckIn.until(fechaCheckOut).getDays();
        return noches * habitacion.getPrecioPorNoche();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    // Personalizar cómo se serializa el cliente a solo su ID
    @JsonProperty("clienteId")
    public Long getClienteId() {
        return cliente != null ? cliente.getId() : null;
    }

    public Habitacion getHabitacion() { return habitacion; }
    public void setHabitacion(Habitacion habitacion) { this.habitacion = habitacion; }

    public LocalDate getFechaCheckIn() { return fechaCheckIn; }
    public void setFechaCheckIn(LocalDate fechaCheckIn) { this.fechaCheckIn = fechaCheckIn; }

    public LocalDate getFechaCheckOut() { return fechaCheckOut; }
    public void setFechaCheckOut(LocalDate fechaCheckOut) { this.fechaCheckOut = fechaCheckOut; }

    public double getTotalPagar() { return totalPagar; }
    public void setTotalPagar(double totalPagar) { this.totalPagar = totalPagar; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public void cancelarReserva() { this.estado = "Cancelada"; }
    public void confirmarReserva() { this.estado = "Confirmada"; }

    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", cliente=" + cliente.getNombre() + " " + cliente.getApellido() +
                ", habitacion=" + habitacion.getNumero() +
                ", fechaCheckIn=" + fechaCheckIn +
                ", fechaCheckOut=" + fechaCheckOut +
                ", totalPagar=" + totalPagar +
                ", estado='" + estado + '\'' +
                ", metodoPago='" + metodoPago + '\'' +
                '}';
    }
}
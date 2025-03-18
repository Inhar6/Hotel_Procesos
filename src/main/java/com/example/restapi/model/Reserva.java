package com.example.restapi.model;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
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

    @Column(nullable = false, length = 20)
    private String metodoPago;

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

    // Método para cancelar la reserva
    public void cancelarReserva() {
        this.estado = "Cancelada";
    }

    // Método para confirmar la reserva
    public void confirmarReserva() {
        this.estado = "Confirmada";
    }

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

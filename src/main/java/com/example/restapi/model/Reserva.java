package com.example.restapi.model;

import java.time.LocalDate;

public class Reserva {
    private Long id;                // Identificador único de la reserva
    private Cliente cliente;        // Cliente que realiza la reserva
    private Habitacion habitacion;  // Habitación reservada
    private LocalDate fechaCheckIn; // Fecha de entrada
    private LocalDate fechaCheckOut;// Fecha de salida
    private double totalPagar;      // Costo total de la reserva
    private String estado;          // Estado de la reserva (Pendiente, Confirmada, Cancelada)
    private String metodoPago;      // Método de pago utilizado

    // Constructor
    public Reserva(Long id, Cliente cliente, Habitacion habitacion, LocalDate fechaCheckIn, LocalDate fechaCheckOut, String metodoPago) {
        this.id = id;
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.fechaCheckIn = fechaCheckIn;
        this.fechaCheckOut = fechaCheckOut;
        this.metodoPago = metodoPago;
        this.estado = "Pendiente"; // Por defecto, la reserva inicia como pendiente
        this.totalPagar = calcularTotal(); // Calcula el total basado en las noches y la tarifa de la habitación
    }

    // Método para calcular el costo total de la reserva
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

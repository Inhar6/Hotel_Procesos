package com.example.restapi.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "check_ins")
public class CheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reserva_id", nullable = false)
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "habitacion_id", nullable = false)
    private Habitacion habitacion;

    @Column(nullable = false)
    private String nombreHuesped;

    @Column(nullable = false)
    private String apellidosHuesped;

    @Column(nullable = false)
    private String tipoDocumento;

    @Column(nullable = false)
    private String numeroDocumento;

    @Column
    private String telefono;

    @Column
    private String email;

    @Column
    private String direccion;

    @Column(nullable = false)
    private LocalDate fechaCheckIn;

    @Column(nullable = false)
    private LocalDate fechaCheckOut;

    @Column(nullable = false)
    private Integer numHuespedes;

    @Column
    private String metodoPago;

    @Column
    private String preferencias;

    @Column
    private String comentarios;

    @Column(nullable = false)
    private boolean activo;

    // Constructor vacío requerido por JPA
    public CheckIn() {
    }

    // Constructor con parámetros
    public CheckIn(Reserva reserva, Habitacion habitacion, String nombreHuesped, String apellidosHuesped,
                  String tipoDocumento, String numeroDocumento, String telefono, String email, String direccion,
                  LocalDate fechaCheckIn, LocalDate fechaCheckOut, Integer numHuespedes, String metodoPago,
                  String preferencias, String comentarios) {
        this.reserva = reserva;
        this.habitacion = habitacion;
        this.nombreHuesped = nombreHuesped;
        this.apellidosHuesped = apellidosHuesped;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.fechaCheckIn = fechaCheckIn;
        this.fechaCheckOut = fechaCheckOut;
        this.numHuespedes = numHuespedes;
        this.metodoPago = metodoPago;
        this.preferencias = preferencias;
        this.comentarios = comentarios;
        this.activo = true;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public String getNombreHuesped() {
        return nombreHuesped;
    }

    public void setNombreHuesped(String nombreHuesped) {
        this.nombreHuesped = nombreHuesped;
    }

    public String getApellidosHuesped() {
        return apellidosHuesped;
    }

    public void setApellidosHuesped(String apellidosHuesped) {
        this.apellidosHuesped = apellidosHuesped;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDate getFechaCheckIn() {
        return fechaCheckIn;
    }

    public void setFechaCheckIn(LocalDate fechaCheckIn) {
        this.fechaCheckIn = fechaCheckIn;
    }

    public LocalDate getFechaCheckOut() {
        return fechaCheckOut;
    }

    public void setFechaCheckOut(LocalDate fechaCheckOut) {
        this.fechaCheckOut = fechaCheckOut;
    }

    public Integer getNumHuespedes() {
        return numHuespedes;
    }

    public void setNumHuespedes(Integer numHuespedes) {
        this.numHuespedes = numHuespedes;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getPreferencias() {
        return preferencias;
    }

    public void setPreferencias(String preferencias) {
        this.preferencias = preferencias;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "CheckIn [id=" + id + ", reserva=" + reserva.getId() + ", habitacion=" + habitacion.getId() 
                + ", nombreHuesped=" + nombreHuesped + ", fechaCheckIn=" + fechaCheckIn 
                + ", fechaCheckOut=" + fechaCheckOut + "]";
    }
}
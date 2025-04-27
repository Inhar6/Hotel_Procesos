package com.example.restapi.model;
// me he ayudado de chat para crear las caracteristicas a utilizar
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Problema {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   private String nombre;
   private String email;
   private String numeroHabitacion;
   private LocalDate fechaReporte;
   private String descripcion;
   private String sugerencias;
   private String urgencia;
   private boolean resuelto;

   // Getters y Setters
   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getNombre() {
      return nombre;
   }

   public void setNombre(String nombre) {
      this.nombre = nombre;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getNumeroHabitacion() {
      return numeroHabitacion;
   }

   public void setNumeroHabitacion(String numeroHabitacion) {
      this.numeroHabitacion = numeroHabitacion;
   }

   public LocalDate getFechaReporte() {
      return fechaReporte;
   }

   public void setFechaReporte(LocalDate fechaReporte) {
      this.fechaReporte = fechaReporte;
   }

   public String getDescripcion() {
      return descripcion;
   }

   public void setDescripcion(String descripcion) {
      this.descripcion = descripcion;
   }

   public String getSugerencias() {
      return sugerencias;
   }

   public void setSugerencias(String sugerencias) {
      this.sugerencias = sugerencias;
   }

   public String getUrgencia() {
      return urgencia;
   }

   public void setUrgencia(String urgencia) {
      this.urgencia = urgencia;
   }

   public boolean isResuelto() {
      return resuelto;
   }

   public void setResuelto(boolean resuelto) {
      this.resuelto = resuelto;
   }
}

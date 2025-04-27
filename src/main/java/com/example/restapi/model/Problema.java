package com.example.restapi.model;
// me he ayudado de chatgpt para ver que caracteristicas poner.
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Problema {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String descripcion;

   private LocalDateTime fechaReporte;

   private boolean resuelto;

   // Getters y Setters
   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getDescripcion() {
      return descripcion;
   }

   public void setDescripcion(String descripcion) {
      this.descripcion = descripcion;
   }

   public LocalDateTime getFechaReporte() {
      return fechaReporte;
   }

   public void setFechaReporte(LocalDateTime fechaReporte) {
      this.fechaReporte = fechaReporte;
   }

   public boolean isResuelto() {
      return resuelto;
   }

   public void setResuelto(boolean resuelto) {
      this.resuelto = resuelto;
   }
}
package com.example.restapi.controller;

import com.example.restapi.model.Problema;
import com.example.restapi.service.ProblemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/problemas")
public class ProblemaController {

   @Autowired
   private ProblemaService problemaService;

   // Cliente reporta un problema
   @PostMapping("/reportar")
   public ResponseEntity<Problema> reportarProblema(@RequestBody Problema problema) {
      Problema problemaReportado = problemaService.reportarProblema(problema);
      return ResponseEntity.ok(problemaReportado);
   }

   // Mantenimiento ve problemas no resueltos
   @GetMapping("/no-resueltos")
   public ResponseEntity<List<Problema>> obtenerProblemasNoResueltos() {
      List<Problema> problemas = problemaService.obtenerProblemasNoResueltos();
      return ResponseEntity.ok(problemas);
   }

   // Mantenimiento marca un problema como resuelto
   @PutMapping("/resolver/{id}")
   public ResponseEntity<String> resolverProblema(@PathVariable Long id) {
      Optional<Problema> problemaOpt = problemaService.obtenerProblemaPorId(id);
      if (problemaOpt.isPresent()) {
         Problema problema = problemaOpt.get();
         problema.setResuelto(true);
         problemaService.guardarProblema(problema);
         return ResponseEntity.ok("Problema marcado como resuelto.");
      } else {
         return ResponseEntity.notFound().build();
      }
   }
}

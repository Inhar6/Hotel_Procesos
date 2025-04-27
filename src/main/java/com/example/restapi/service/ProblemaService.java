package com.example.restapi.service;

import com.example.restapi.model.Problema;
import java.time.LocalDate;
import com.example.restapi.repository.ProblemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProblemaService {

   @Autowired
   private ProblemaRepository problemaRepository;

   public Problema reportarProblema(Problema problema) {
      problema.setFechaReporte(LocalDate.now());
      problema.setResuelto(false);
      return problemaRepository.save(problema);
   }

   public List<Problema> obtenerProblemasNoResueltos() {
      return problemaRepository.findByResueltoFalse();
   }

   public Optional<Problema> obtenerProblemaPorId(Long id) {
      return problemaRepository.findById(id);
   }

   public void guardarProblema(Problema problema) {
      problemaRepository.save(problema);
   }
}

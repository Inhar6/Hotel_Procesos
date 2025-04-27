package com.example.restapi.repository;

import com.example.restapi.model.Problema;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProblemaRepository extends JpaRepository<Problema, Long> {

   List<Problema> findByResueltoFalse();
}

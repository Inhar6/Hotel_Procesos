package com.example.restapi;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import com.example.restapi.repository.*;
import com.example.restapi.model.*;

@Component
public class DataLoader {

    private final ClienteRepository clienteRepository;
    private final HabitacionRepository habitacionRepository;
    private final ReservaRepository reservaRepository;

    @Autowired
    public DataLoader(ClienteRepository clienteRepository, 
                      HabitacionRepository habitacionRepository, 
                      ReservaRepository reservaRepository) {
        this.clienteRepository = clienteRepository;
        this.habitacionRepository = habitacionRepository;
        this.reservaRepository = reservaRepository;
    }

    // Método para cargar datos iniciales en la base de datos
    @Transactional
    public void loadData() {
        System.out.println("Cargando datos iniciales...");

        // Verificar si ya hay datos
        if (clienteRepository.count() > 0 || habitacionRepository.count() > 0 || reservaRepository.count() > 0) {
            System.out.println("Los datos ya existen en la base de datos. No se insertarán duplicados.");
            return;
        }

        // Crear Clientes
        Cliente cliente1 = new Cliente("Juan", "Pérez", "juan@example.com", "123456789", "Tarjeta");
        Cliente cliente2 = new Cliente("María", "Gómez", "maria@example.com", "987654321", "PayPal");
        Cliente cliente3 = new Cliente("Carlos", "López", "carlos@example.com", "1122334455", "Transferencia");

        clienteRepository.saveAll(List.of(cliente1, cliente2, cliente3));
        clienteRepository.flush(); // Sincronizar con la base de datos

        // Crear Habitaciones
        Habitacion habitacion1 = new Habitacion(101, "Individual", 50.0, true, true);
        Habitacion habitacion2 = new Habitacion(102, "Doble", 80.0, false, true);
        Habitacion habitacion3 = new Habitacion(103, "Suite", 150.0, true, false);
        Habitacion habitacion4 = new Habitacion(104, "Doble", 85.0, true, true);

        habitacionRepository.saveAll(List.of(habitacion1, habitacion2, habitacion3, habitacion4));
        habitacionRepository.flush(); // Sincronizar con la base de datos

        // Crear Reservas (usamos IDs generados por Hibernate)
        Reserva reserva1 = new Reserva(cliente1, habitacion1, LocalDate.now(), LocalDate.now().plusDays(3), "Tarjeta");
        Reserva reserva2 = new Reserva(cliente2, habitacion2, LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), "PayPal");
        Reserva reserva3 = new Reserva(cliente3, habitacion4, LocalDate.now().minusDays(2), LocalDate.now().plusDays(1), "Transferencia");

        reservaRepository.saveAll(List.of(reserva1, reserva2, reserva3));

        System.out.println("Datos iniciales cargados con éxito.");
    }

    // Ejecución automática al iniciar la aplicación
    @PostConstruct
    public void init() {
        loadData();
    }
}

package com.example.restapi;
/* 
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

        // Crear Clientes
        Cliente cliente1 = new Cliente("Juan", "Pérez", "juan@example.com", "123456789", "Tarjeta de Crédito");
        Cliente cliente2 = new Cliente("María", "Gómez", "maria@example.com", "987654321", "PayPal");
        Cliente cliente3 = new Cliente("Carlos", "López", "carlos@example.com", "1122334455", "Transferencia Bancaria");

        clienteRepository.saveAll(List.of(cliente1, cliente2, cliente3));

        // Crear Habitaciones
        Habitacion habitacion1 = new Habitacion(101, "Individual", 50.0, true, true);
        Habitacion habitacion2 = new Habitacion(102, "Doble", 80.0, false, true);
        Habitacion habitacion3 = new Habitacion(103, "Suite",150.0, true, false);
        Habitacion habitacion4 = new Habitacion(104, "Doble",85.0, true, true);

        habitacionRepository.saveAll(List.of(habitacion1, habitacion2, habitacion3, habitacion4));

        // Crear Reservas
        Reserva reserva1 = new Reserva(cliente1, habitacion1, LocalDate.now(), LocalDate.now().plusDays(3), "Tarjeta de Crédito");
        Reserva reserva2 = new Reserva(cliente2, habitacion2, LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), "PayPal");
        Reserva reserva3 = new Reserva(cliente3, habitacion4, LocalDate.now().minusDays(2), LocalDate.now().plusDays(1), "Transferencia Bancaria");

        reservaRepository.saveAll(List.of(reserva1, reserva2, reserva3));

        System.out.println("Datos iniciales cargados con éxito.");
    }

    // Ejecución automática al iniciar la aplicación
    @PostConstruct
    public void init() {
        loadData();
    }
}
*/
package py.edu.ucom;

import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date; // Asegúrate de importar java.util.Date
import java.util.List;
import py.edu.ucom.repository.PresupuestoRepository;
import py.edu.ucom.entities.apiresponse.Presupuesto;
import py.edu.ucom.entities.apiresponse.Gastos;

@ApplicationScoped
public class Main implements QuarkusApplication {

    @Inject
    PresupuestoRepository repository;

    @Override
    public int run(String... args) {
        // Listar todos los presupuestos
        System.out.println("Listado de presupuestos:");
        List<Presupuesto> presupuestos = repository.listar();
        for (Presupuesto p : presupuestos) {
            System.out.println(p);
        }

        // Obtener un presupuesto por ID
        int id = 1; // Cambia este ID según tus datos
        Presupuesto presupuesto = repository.obtenerById(id);
        System.out.println("Presupuesto con ID " + id + ": " + presupuesto);

        // Agregar un nuevo gasto a un presupuesto
        if (presupuesto != null) {
            // Datos del nuevo gasto
            LocalDate localDate = LocalDate.of(2024, 8, 23); // Fecha en LocalDate
            Date fecha = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()); // Convertir LocalDate a
                                                                                                // Date

            Gastos nuevoGasto = new Gastos();
            nuevoGasto.setId(4); // Nuevo ID para el gasto
            nuevoGasto.setFecha(fecha); // Establecer la fecha como Date
            nuevoGasto.setMonto(5000.0); // Asegúrate de que esto sea un Double
            nuevoGasto.setDescripcion("Nuevo gasto");

            // Agregar el gasto al presupuesto
            Presupuesto actualizado = repository.agregarGasto(id, nuevoGasto);
            System.out.println("Presupuesto actualizado: " + actualizado);
        }

        // Modificar un presupuesto existente
        if (presupuesto != null) {
            presupuesto.setMontoPresupuestado(120000.0); // Usa Double en lugar de int
            Presupuesto modificado = repository.modificar(presupuesto);
            System.out.println("Presupuesto modificado: " + modificado);
        }

        // Eliminar un presupuesto por ID
        int eliminarId = 2; // Cambia este ID según tus datos
        repository.eliminar(eliminarId);
        System.out.println("Presupuesto con ID " + eliminarId + " eliminado.");

        // Volver a listar todos los presupuestos después de la modificación
        System.out.println("Listado de presupuestos después de modificaciones:");
        List<Presupuesto> presupuestosActualizados = repository.listar();
        for (Presupuesto p : presupuestosActualizados) {
            System.out.println(p);
        }

        return 0; // Devuelve 0 si todo está bien
    }

    public static void main(String[] args) {
        Quarkus.run(Main.class, args);
    }
}

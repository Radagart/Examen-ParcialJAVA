package py.edu.ucom.services;

import py.edu.ucom.entities.apiresponse.Gastos;
import py.edu.ucom.entities.apiresponse.Presupuesto;
import py.edu.ucom.repository.PresupuestoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class PresupuestoService {

    @Inject
    public PresupuestoRepository presupuestoRepository;

    public Presupuesto crearPresupuesto(Presupuesto presupuesto) {
        // Lógica para crear un nuevo presupuesto
        presupuestoRepository.agregar(presupuesto);
        return presupuesto;
    }

    public List<Presupuesto> listarPresupuestos() {
        // Lógica para listar todos los presupuestos
        return presupuestoRepository.listar();
    }

    public Presupuesto obtenerPresupuesto(Integer id) {
        // Lógica para obtener un presupuesto por su ID
        return presupuestoRepository.obtenerById(id);
    }

    public double calcularTotalGastos(Integer presupuestoId) {
        Presupuesto presupuesto = presupuestoRepository.obtenerById(presupuestoId);
        if (presupuesto != null) {
            return presupuesto.getGastos().stream()
                    .mapToDouble(Gastos::getMonto)
                    .sum();
        }
        return 0.0;
    }

    public boolean agregarGasto(Integer presupuestoId, Gastos gasto) {
        Presupuesto presupuesto = presupuestoRepository.obtenerById(presupuestoId);
        if (presupuesto != null) {
            // Calcula el total de gastos actuales
            double totalGastos = presupuesto.getGastos().stream()
                    .mapToDouble(Gastos::getMonto)
                    .sum();
            // Verifica si el nuevo gasto supera el monto presupuestado
            if (totalGastos + gasto.getMonto() <= presupuesto.getMontoPresupuestado()) {
                presupuestoRepository.agregarGasto(presupuestoId, gasto);
                return true;
            }
        }
        return false; // El gasto supera el monto presupuestado o el presupuesto no existe
    }

}

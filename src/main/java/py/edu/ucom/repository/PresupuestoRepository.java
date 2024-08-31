package py.edu.ucom.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import py.edu.ucom.entities.apiresponse.Presupuesto;
import py.edu.ucom.entities.apiresponse.Gastos;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class PresupuestoRepository {
    private List<Presupuesto> presupuestosList; // Lista de presupuestos

    @Inject
    ConfigProperties configProperties;

    public PresupuestoRepository() {
        presupuestosList = new ArrayList<>();
        cargarDatos(); // Cargar los datos al inicializar
    }

    // Configurar ObjectMapper para manejar LocalDate
    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Registra el módulo para Java 8 Date/Time
        return objectMapper;
    }

    // Cargar los datos desde el archivo JSON
    public void cargarDatos() {
        ObjectMapper objectMapper = createObjectMapper(); // Usa el método para crear ObjectMapper
        String filePath = configProperties.getFilePath(); // Usar el path del archivo desde ConfigProperties
        try {
            File file = new File(filePath);
            if (file.exists()) {
                presupuestosList = objectMapper.readValue(file, new TypeReference<List<Presupuesto>>() {
                });
            } else {
                presupuestosList = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Guardar los datos en el archivo JSON
    public void guardarDatos() {
        ObjectMapper objectMapper = createObjectMapper(); // Usa el método para crear ObjectMapper
        String filePath = configProperties.getFilePath(); // Usar el path del archivo desde ConfigProperties
        try {
            objectMapper.writeValue(new File(filePath), presupuestosList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listarPresupuestos() {
        System.out.println("Listado de presupuestos:");
        for (Presupuesto p : presupuestosList) {
            System.out.println(p);
        }
    }

    // Agregar un nuevo presupuesto
    public Presupuesto agregar(Presupuesto presupuesto) {
        if (presupuestosList.stream().noneMatch(p -> p.getId().equals(presupuesto.getId()))) { // Comparación con
                                                                                               // equals()
            presupuestosList.add(presupuesto);
            guardarDatos(); // Guardar cambios en el archivo JSON
            return presupuesto;
        } else {
            return null; // Presupuesto ya existe
        }
    }

    // Obtener un presupuesto por su ID
    public Presupuesto obtenerById(Integer id) {
        return presupuestosList.stream()
                .filter(presupuesto -> presupuesto.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Listar todos los presupuestos
    public List<Presupuesto> listar() {
        return new ArrayList<>(presupuestosList);
    }

    // Agregar un nuevo gasto a un presupuesto específico
    public Presupuesto agregarGasto(Integer presupuestoId, Gastos gasto) {
        Optional<Presupuesto> presupuestoOpt = presupuestosList.stream()
                .filter(p -> p.getId().equals(presupuestoId))
                .findFirst();

        if (presupuestoOpt.isPresent()) {
            Presupuesto presupuesto = presupuestoOpt.get();
            presupuesto.getGastos().add(gasto);
            guardarDatos(); // Guardar cambios en el archivo JSON
            return presupuesto;
        } else {
            return null;
        }
    }

    // Modificar un presupuesto existente
    public Presupuesto modificar(Presupuesto presupuestoModificado) {
        Optional<Presupuesto> existingPresupuesto = presupuestosList.stream()
                .filter(p -> p.getId().equals(presupuestoModificado.getId()))
                .findFirst();

        if (existingPresupuesto.isPresent()) {
            presupuestosList = presupuestosList.stream()
                    .map(p -> p.getId().equals(presupuestoModificado.getId()) ? presupuestoModificado : p)
                    .collect(Collectors.toList());
            guardarDatos(); // Guardar cambios en el archivo JSON
            return presupuestoModificado;
        } else {
            return null;
        }
    }

    // Eliminar un presupuesto por su ID
    public void eliminar(Integer id) {
        presupuestosList = presupuestosList.stream()
                .filter(p -> !p.getId().equals(id))
                .collect(Collectors.toList());
        guardarDatos(); // Guardar cambios en el archivo JSON
    }
}

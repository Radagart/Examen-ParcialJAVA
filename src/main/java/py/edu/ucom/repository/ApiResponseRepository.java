package py.edu.ucom.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import py.edu.ucom.entities.apiresponse.Gastos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ApiResponseRepository {

    private List<Gastos> gastosList;
    private String filePath;

    @PostConstruct
    public void init() {
        filePath = Paths.get("src", "main", "resources", "gastos.json").toString();
        gastosList = new ArrayList<>();
        cargarDatos();
    }

    public void cargarDatos() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File(filePath);
            if (file.exists()) {
                gastosList = objectMapper.readValue(file, new TypeReference<List<Gastos>>() {
                });
            } else {
                gastosList = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void guardarDatos() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(filePath), gastosList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Gastos obtenerById(Integer id) {
        return gastosList.stream()
                .filter(gasto -> gasto.getId() != null && gasto.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Gastos agregar(Gastos gasto) {
        gastosList.add(gasto);
        guardarDatos();
        return gasto;
    }

    public void eliminar(Integer id) {
        gastosList = gastosList.stream()
                .filter(gasto -> gasto.getId() == null || !gasto.getId().equals(id))
                .collect(Collectors.toList());
        guardarDatos();
    }

    public List<Gastos> listar() {
        return new ArrayList<>(gastosList);
    }

    public Gastos modificar(Gastos gastoModificado) {
        Optional<Gastos> existingGastoOpt = gastosList.stream()
                .filter(gasto -> gasto.getId() != null && gasto.getId().equals(gastoModificado.getId()))
                .findFirst();

        if (existingGastoOpt.isPresent()) {
            gastosList = gastosList.stream()
                    .map(gasto -> gasto.getId() != null && gasto.getId().equals(gastoModificado.getId())
                            ? gastoModificado
                            : gasto)
                    .collect(Collectors.toList());
            guardarDatos();
            return gastoModificado;
        } else {
            return null;
        }
    }
}

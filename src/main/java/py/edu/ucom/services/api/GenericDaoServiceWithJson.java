package py.edu.ucom.services.api;

import jakarta.inject.Inject;
import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import py.edu.ucom.entities.apiresponse.Gastos;
import py.edu.ucom.repository.ApiResponseRepository;
import py.edu.ucom.config.GenericDAO;

@ApplicationScoped
public class GenericDaoServiceWithJson implements GenericDAO<Gastos, Integer> {

    @Inject
    private ApiResponseRepository repository;

    @Override
    public List<Gastos> listar() {
        return this.repository.listar();
    }

    @Override
    public Gastos obtener(Integer id) {
        return this.repository.obtenerById(id);
    }

    @Override
    public Gastos modificar(Gastos param) {
        return this.repository.modificar(param);
    }

    @Override
    public Gastos agregar(Gastos param) {
        return this.repository.agregar(param);
    }

    @Override
    public void eliminar(Integer id) {
        this.repository.eliminar(id);
    }

    public Gastos mayorGasto() {
        List<Gastos> lista = this.repository.listar();
        double max = 0;
        Gastos mayorGasto = null;
        for (Gastos elem : lista) {
            if (elem.getMonto() > max) {
                max = elem.getMonto();
                mayorGasto = elem;
            }
        }
        return mayorGasto;
    }
}

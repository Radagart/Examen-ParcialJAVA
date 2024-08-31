package py.edu.ucom.config;

import java.util.List;

public interface GenericDAO<T, ID> {
    List<T> listar();

    T obtener(ID id);

    T modificar(T param);

    T agregar(T param);

    void eliminar(ID id);
}

package py.edu.ucom.controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;

import py.edu.ucom.entities.apiresponse.Gastos;
import py.edu.ucom.entities.apiresponse.Presupuesto;
import py.edu.ucom.model.response.ApiResponse;
import py.edu.ucom.services.PresupuestoService;
import py.edu.ucom.repository.PresupuestoRepository;

import java.util.List;

@Path("/presupuestos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PresupuestoController {

    @Inject
    private PresupuestoService presupuestoService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse<Presupuesto> crearPresupuesto(Presupuesto presupuesto) {
        ApiResponse<Presupuesto> respuesta = new ApiResponse<>();
        try {
            Presupuesto nuevoPresupuesto = presupuestoService.crearPresupuesto(presupuesto);
            respuesta.setCode(Response.Status.CREATED.getStatusCode());
            respuesta.setMessage("Presupuesto creado exitosamente");
            respuesta.setData(nuevoPresupuesto);
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.setCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
            respuesta.setMessage("Error al crear el presupuesto");
            respuesta.setData(null);
        }
        return respuesta;
    }

    @GET
    @Path("/total-gastos/{presupuestoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse<Double> obtenerTotalGastos(@PathParam("presupuestoId") Integer presupuestoId) {
        ApiResponse<Double> respuesta = new ApiResponse<>();
        try {
            double totalGastos = presupuestoService.calcularTotalGastos(presupuestoId);
            respuesta.setCode(Response.Status.OK.getStatusCode());
            respuesta.setMessage("Total de gastos obtenido exitosamente");
            respuesta.setData(totalGastos);
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.setCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
            respuesta.setMessage("Error al obtener el total de gastos");
            respuesta.setData(null);
        }
        return respuesta;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse<List<Presupuesto>> listarPresupuestos() {
        ApiResponse<List<Presupuesto>> respuesta = new ApiResponse<>();
        try {
            List<Presupuesto> presupuestos = presupuestoService.listarPresupuestos();
            respuesta.setCode(Response.Status.OK.getStatusCode());
            respuesta.setMessage("Listado de presupuestos");
            respuesta.setData(presupuestos);
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.setCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
            respuesta.setMessage("Error al listar los presupuestos");
            respuesta.setData(null);
        }
        return respuesta;
    }

    @GET
    @Path("/{presupuestoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse<Presupuesto> obtenerPresupuesto(@PathParam("presupuestoId") Integer presupuestoId) {
        ApiResponse<Presupuesto> respuesta = new ApiResponse<>();
        try {
            Presupuesto presupuesto = presupuestoService.obtenerPresupuesto(presupuestoId);
            if (presupuesto != null) {
                respuesta.setCode(Response.Status.OK.getStatusCode());
                respuesta.setMessage("Presupuesto encontrado");
                respuesta.setData(presupuesto);
            } else {
                respuesta.setCode(Response.Status.NOT_FOUND.getStatusCode());
                respuesta.setMessage("Presupuesto no encontrado");
                respuesta.setData(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.setCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
            respuesta.setMessage("Error al obtener el presupuesto: " + e.getMessage());
            respuesta.setData(null);
        }
        return respuesta;
    }

    @POST
    @Path("/agregar-gasto/{presupuestoId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse<Void> agregarGasto(@PathParam("presupuestoId") Integer presupuestoId, Gastos gasto) {
        ApiResponse<Void> respuesta = new ApiResponse<>();
        try {
            if (gasto == null || gasto.getMonto() <= 0) {
                respuesta.setCode(Response.Status.BAD_REQUEST.getStatusCode());
                respuesta.setMessage("El gasto debe ser mayor que cero.");
                respuesta.setData(null);
                return respuesta;
            }

            boolean agregado = presupuestoService.agregarGasto(presupuestoId, gasto);
            if (agregado) {
                respuesta.setCode(Response.Status.OK.getStatusCode());
                respuesta.setMessage("Gasto agregado exitosamente");
                respuesta.setData(null);
            } else {
                respuesta.setCode(Response.Status.BAD_REQUEST.getStatusCode());
                respuesta.setMessage("El gasto supera el monto presupuestado o el presupuesto no existe.");
                respuesta.setData(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.setCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
            respuesta.setMessage("Error al agregar el gasto: " + e.getMessage());
            respuesta.setData(null);
        }
        return respuesta;
    }
}
package convention.rest.api;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import convention.persistent.Reunion;
import convention.persistent.StatusDeReunion;
import convention.persistent.TemaDeReunion;
import convention.rest.api.tos.PropuestaDePinoARootTo;
import convention.rest.api.tos.ReunionTo;
import convention.services.ReunionService;
import convention.services.TemaGeneralService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * This type is the resource API for users
 * Created by kfgodel on 03/03/15.
 */
@Produces("application/json")
@Consumes("application/json")
public class ReunionResource {

    @Inject
    private ReunionService reunionService;

    @Inject
    private TemaGeneralService temaGeneralService;

    private ResourceHelper resourceHelper;

    private static final Type LISTA_DE_REUNIONES_TO = new ReferenceOf<List<ReunionTo>>() {
    }.getReferencedType();


    public Reunion muestreoDeReunion(Reunion reunion, Long userId, SecurityContext securityContext) {
        Reunion nuevaReunion = reunion.copy();

        if (reunion.getStatus() == StatusDeReunion.PENDIENTE) {
            List<TemaDeReunion> listaDeTemasNuevos = reunion.getTemasPropuestos().stream().
                    map(temaDeReunion ->
                            temaDeReunion.copy()).collect(Collectors.toList());
            listaDeTemasNuevos.forEach(temaDeReunion -> temaDeReunion.ocultarVotosPara(userId));
            listaDeTemasNuevos.sort(Comparator.comparing(TemaDeReunion::getId));
            Collections.shuffle(listaDeTemasNuevos, new Random(securityContext.getUserPrincipal().hashCode())); //random turbio
            nuevaReunion.setTemasPropuestos(listaDeTemasNuevos);
        }
        return nuevaReunion;
    }

    @GET
    @Path("proxima")
    public ReunionTo getProxima(@Context SecurityContext securityContext) {
        Reunion proxima = reunionService.getProxima();
        return getSingle(proxima.getId(), securityContext);
    }

    @GET
    @Path("cerrar/{resourceId}")
    public ReunionTo cerrar(@PathParam("resourceId") Long id) {
        Reunion reunionCerrada = reunionService.updateAndMapping(id,
                reunion -> {
                    reunion.cerrarVotacion();
                    return reunion;
                });
         return getResourceHelper().convertir(reunionCerrada, ReunionTo.class);
    }


    @GET
    @Path("reabrir/{resourceId}")
    public ReunionTo reabrir(@PathParam("resourceId") Long id) {
        Reunion reunionAbierta = reunionService.updateAndMapping(id,
                reunion -> {
                    reunion.reabrirVotacion();
                    return reunion;
                });

        return getResourceHelper().convertir(reunionAbierta, ReunionTo.class);
    }

    @GET
    public List<ReunionTo> getAll(@Context SecurityContext securityContext) {
        Long userId = getResourceHelper().idDeUsuarioActual(securityContext);
        List<Reunion> reuniones = reunionService.getAll();
        List<Reunion> reunionesFiltradas = reuniones.stream()
                .map(reunion -> muestreoDeReunion(reunion, userId, securityContext)).collect(Collectors.toList());
        return getResourceHelper().convertir(reunionesFiltradas, LISTA_DE_REUNIONES_TO);
    }

    @POST
    public ReunionTo create(ReunionTo reunionNuevaTo) {

        Reunion nuevaReunion = getResourceHelper().convertir(reunionNuevaTo, Reunion.class);
        nuevaReunion.agregarTemasGenerales(temaGeneralService.getAll());
        Reunion reunionCreada = reunionService.save(nuevaReunion);
        return getResourceHelper().convertir(reunionCreada, ReunionTo.class);
    }

    @GET
    @Path("/{resourceId}")
    public ReunionTo getSingle(@PathParam("resourceId") Long id, @Context SecurityContext securityContext) {
        Long userId = getResourceHelper().idDeUsuarioActual(securityContext);
        Reunion reunionFiltrada = reunionService.getAndMapping(id, reunion -> muestreoDeReunion(reunion, userId, securityContext));
        return getResourceHelper().convertir(reunionFiltrada, ReunionTo.class);
    }


    @PUT
    @Path("/{resourceId}")
    public ReunionTo update(ReunionTo newState, @PathParam("resourceId") Long id) {
        Reunion reunionActualizada = reunionService.update(getResourceHelper().convertir(newState, Reunion.class));
        return getResourceHelper().convertir(reunionActualizada, ReunionTo.class);
    }

    @DELETE
    @Path("/{resourceId}")
    public void delete(@PathParam("resourceId") Long id) {
        reunionService.delete(id);
    }

    public static ReunionResource create(DependencyInjector appInjector) {
        ReunionResource reunionResource = new ReunionResource();
        reunionResource.resourceHelper= ResourceHelper.create(appInjector);
        reunionResource.getResourceHelper().bindAppInjectorTo(ReunionResource.class,reunionResource);
        reunionResource.reunionService = appInjector.createInjected(ReunionService.class);
        return reunionResource;
    }

    @POST
    @Path("/{resourceId}/propuestas")
    public Response proponerPinoComoRoot(
            @PathParam("resourceId") Long id,
            PropuestaDePinoARootTo propuesta,
            @Context SecurityContext securityContext) {

        Reunion reunion = reunionService.get(id);
        reunion.proponerPinoComoRoot(propuesta.getPino(), getResourceHelper().usuarioActual(securityContext));
        reunionService.save(reunion);

        ReunionTo reunionTo = getResourceHelper().convertir(reunion, ReunionTo.class);

        return Response.status(HttpServletResponse.SC_CREATED).entity(reunionTo).build();
    }

    public ResourceHelper getResourceHelper() {
        return resourceHelper;
    }

}

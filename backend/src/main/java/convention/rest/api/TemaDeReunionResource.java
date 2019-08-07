package convention.rest.api;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import convention.persistent.TemaDeReunion;
import convention.persistent.Usuario;
import convention.rest.api.tos.TemaDeReunionTo;
import convention.rest.api.tos.TemaEnCreacionTo;
import convention.rest.api.tos.TemaTo;
import convention.services.TemaService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * Esta clase representa el recurso rest para modificar temas
 * Created by kfgodel on 03/03/15.
 */
@Produces("application/json")
@Consumes("application/json")
public class TemaDeReunionResource {

    @Inject
    TemaService temaService;

    private ResourceHelper resourceHelper;
    @POST
    public TemaTo create(TemaEnCreacionTo newState,@Context SecurityContext securityContext) {
        TemaDeReunion temaCreado = getResourceHelper().convertir(newState, TemaDeReunion.class);
        Usuario modificador = getResourceHelper().usuarioActual(securityContext);
        temaCreado.setUltimoModificador(modificador);
        temaService.save(temaCreado);
        return getResourceHelper().convertir(temaCreado, TemaTo.class);
    }

    @Path("/{resourceId}")
    @PUT
    public TemaTo update(TemaTo newState,@PathParam("resourceId") Long id,@Context SecurityContext securityContext) {
        TemaDeReunion estadoNuevo=getResourceHelper().convertir(newState, TemaDeReunion.class);
        Usuario modificador=getResourceHelper().usuarioActual(securityContext);
                    estadoNuevo.setUltimoModificador(modificador);
        TemaDeReunion temaUpdateado = temaService.update(estadoNuevo);
        return getResourceHelper().convertir(temaUpdateado, TemaTo.class);
    }
    @GET
    @Path("/{resourceId}")
    public TemaDeReunionTo getSingle(@PathParam("resourceId") Long id) {
        return getResourceHelper().convertir(temaService.get(id), TemaDeReunionTo.class);
    }

    @GET
    @Path("votar/{resourceId}")
    public TemaTo votar(@PathParam("resourceId") Long id, @Context SecurityContext securityContext) {

        Usuario usuarioActual = getResourceHelper().usuarioActual(securityContext);

        TemaDeReunion temaVotado = temaService.updateAndMapping(id,
                temaDeReunion -> votarTema(usuarioActual, temaDeReunion));
        return getResourceHelper().convertir(temaVotado, TemaTo.class);
    }

    public TemaDeReunion votarTema(Usuario usuarioActual, TemaDeReunion temaDeReunion) {

        long cantidadDeVotos = temaDeReunion.getInteresados().stream()
                .filter(usuario ->
                        usuario.getId().equals(usuarioActual.getId())).count();
        if (cantidadDeVotos >= 3) {
            throw new WebApplicationException("excede la cantidad de votos permitidos", 409);
        }
        try {
            temaDeReunion.agregarInteresado(usuarioActual);
        } catch (Exception exception) {
            throw new WebApplicationException(TemaDeReunion.mensajeDeErrorAlAgregarInteresado(), 409);
        }
        return temaDeReunion;
    }

    @GET
    @Path("desvotar/{resourceId}")
    public TemaTo desvotar(@PathParam("resourceId") Long id, @Context SecurityContext securityContext) {

        Usuario usuarioActual = getResourceHelper().usuarioActual(securityContext);

        TemaDeReunion temaVotado = temaService.updateAndMapping(id,
                temaDeReunion -> desvotarTema(usuarioActual, temaDeReunion)
        );
        return getResourceHelper().convertir(temaVotado, TemaTo.class);

    }

    public TemaDeReunion desvotarTema(Usuario usuarioActual, TemaDeReunion temaDeReunion) {
        long cantidadDeVotos = temaDeReunion.getInteresados().stream()
                .filter(usuario ->
                        usuario.getId().equals(usuarioActual.getId())).count();
        if (cantidadDeVotos <= 0) {
            throw new WebApplicationException("el usuario no tiene votos en el tema", 409);
        }
        temaDeReunion.quitarInteresado(usuarioActual);
        return temaDeReunion;
    }

    @DELETE
    @Path("/{resourceId}")
    public void delete(@PathParam("resourceId") Long id) {

        temaService.delete(id);
    }

    public static TemaDeReunionResource create(DependencyInjector appInjector) {
        TemaDeReunionResource temaDeReunionResource = new TemaDeReunionResource();
        temaDeReunionResource.resourceHelper= ResourceHelper.create(appInjector);
        temaDeReunionResource.getResourceHelper().bindAppInjectorTo(TemaDeReunionResource.class,temaDeReunionResource);
        temaDeReunionResource.temaService = appInjector.createInjected(TemaService.class);
         return temaDeReunionResource;
    }

    public ResourceHelper getResourceHelper() {
        return resourceHelper;
    }
}

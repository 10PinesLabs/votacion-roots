package convention.rest.api;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import convention.persistent.TemaDeReunion;
import convention.persistent.TemaDeReunionConDescripcion;
import convention.persistent.TemaParaProponerPinosARoot;
import convention.persistent.Usuario;
import convention.rest.api.tos.TemaDeReunionTo;
import convention.rest.api.tos.TemaEnCreacionTo;
import convention.services.TemaService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * Esta clase representa el recurso rest para modificar temas
 * Created by kfgodel on 03/03/15.
 */
@Produces("application/json")
@Consumes("application/json")
public class TemaDeReunionResource {

    @Inject
    private TemaService temaService;

    private ResourceHelper resourceHelper;

    @POST
    public TemaDeReunionTo create(TemaEnCreacionTo newState, @Context SecurityContext securityContext) {
        TemaDeReunionConDescripcion temaCreado = getResourceHelper().convertir(newState, TemaDeReunionConDescripcion.class);
        validarTemaDeReunionConDescripcion(temaCreado);
        temaCreado.setUltimoModificador(getResourceHelper().usuarioActual(securityContext));
        TemaDeReunion temaDeReunion = temaService.save(temaCreado);
        return getResourceHelper().convertir(temaDeReunion, TemaDeReunionTo.class);
    }

    @PUT
    @Path("/{resourceId}")
    public TemaDeReunionTo update(TemaDeReunionTo newState, @PathParam("resourceId") Long id, @Context SecurityContext securityContext) {
        TemaDeReunionConDescripcion temaCreado = getResourceHelper().convertir(newState, TemaDeReunionConDescripcion.class);
        validarTemaDeReunionConDescripcion(temaCreado);
        temaCreado.setUltimoModificador(getResourceHelper().usuarioActual(securityContext));
        return temaService.updating(temaCreado).convertTo(TemaDeReunionTo.class);
    }

    @GET
    @Path("/{resourceId}")
    public TemaDeReunionTo getSingle(@PathParam("resourceId") Long id) {
        return temaService.getting(id).convertTo(TemaDeReunionTo.class);
    }

    @GET
    @Path("votar/{resourceId}")
    public TemaDeReunionTo votar(@PathParam("resourceId") Long id, @Context SecurityContext securityContext) {

        Usuario usuarioActual = getResourceHelper().usuarioActual(securityContext);

        return temaService
            .updatingAndMapping(id, temaDeReunion -> votarTema(usuarioActual, temaDeReunion))
            .convertTo(TemaDeReunionTo.class);
    }

    public TemaDeReunion votarTema(Usuario usuarioActual, TemaDeReunion temaDeReunion) {

        long cantidadDeVotos = temaDeReunion.getInteresados().stream()
            .filter(usuario ->
                usuario.getId().equals(usuarioActual.getId())).count();
        if (cantidadDeVotos >= 3) {
            throw new WebApplicationException("excede la cantidad de votos permitidos", Response.Status.CONFLICT);
        }
        try {
            temaDeReunion.agregarInteresado(usuarioActual);
        } catch (Exception exception) {
            throw new WebApplicationException(TemaDeReunion.ERROR_AGREGAR_INTERESADO, Response.Status.CONFLICT);
        }
        return temaDeReunion;
    }

    @GET
    @Path("desvotar/{resourceId}")
    public TemaDeReunionTo desvotar(@PathParam("resourceId") Long id, @Context SecurityContext securityContext) {

        Usuario usuarioActual = getResourceHelper().usuarioActual(securityContext);

        return temaService
            .updatingAndMapping(id, temaDeReunion -> desvotarTema(usuarioActual, temaDeReunion))
            .convertTo(TemaDeReunionTo.class);
    }

    public TemaDeReunion desvotarTema(Usuario usuarioActual, TemaDeReunion temaDeReunion) {
        long cantidadDeVotos = temaDeReunion.getInteresados().stream()
            .filter(usuario ->
                usuario.getId().equals(usuarioActual.getId())).count();
        if (cantidadDeVotos <= 0) {
            throw new WebApplicationException("el usuario no tiene votos en el tema", Response.Status.CONFLICT);
        }
        temaDeReunion.quitarInteresado(usuarioActual);
        return temaDeReunion;
    }

    @DELETE
    @Path("/{resourceId}")
    public void delete(@PathParam("resourceId") Long id) {
        TemaDeReunion tema = temaService.get(id);
        temaService.convertirRePropuestasAPropuestasOriginales(id);
        temaService.delete(tema);
    }

    public static TemaDeReunionResource create(DependencyInjector appInjector) {
        TemaDeReunionResource temaDeReunionResource = new TemaDeReunionResource();
        temaDeReunionResource.resourceHelper = ResourceHelper.create(appInjector);
        temaDeReunionResource.getResourceHelper().bindAppInjectorTo(TemaDeReunionResource.class, temaDeReunionResource);
        temaDeReunionResource.temaService = appInjector.createInjected(TemaService.class);
        return temaDeReunionResource;
    }

    public ResourceHelper getResourceHelper() {
        return resourceHelper;
    }

    private void validarTemaDeReunionConDescripcion(TemaDeReunionConDescripcion nuevoTema) {
        verificarQueNoTieneTituloDeTemaParaProponerPinosARoot(nuevoTema);
        verificarQueNoReProponeUnaRePropuesta(nuevoTema);
        verificarQueNoHayOtroTemaEnLaReunionQueTrataLaMismaPropuesta(nuevoTema);
    }

    private void verificarQueNoTieneTituloDeTemaParaProponerPinosARoot(TemaDeReunionConDescripcion unTemaDeReunion) {
        if (unTemaDeReunion.getTitulo().equals(TemaParaProponerPinosARoot.TITULO)) {
            throw new WebApplicationException("No puede haber 2 temas de proponer pino a roots", Response.Status.CONFLICT);
        }
    }

    private void verificarQueNoReProponeUnaRePropuesta(TemaDeReunionConDescripcion unTemaDeReunion) {
        unTemaDeReunion.propuestaOriginal().ifPresent(propuestaOriginal -> {
            if (propuestaOriginal.getEsRePropuesta()) {
                throw new WebApplicationException("No se puede volver a proponer una re-propuesta", Response.Status.BAD_REQUEST);
            }
        });
    }

    private void verificarQueNoHayOtroTemaEnLaReunionQueTrataLaMismaPropuesta(TemaDeReunionConDescripcion unTemaDeReunion) {
        if (unTemaDeReunion.getReunion().tieneOtroTemaQueTrataLaMismaPropuestaQue(unTemaDeReunion)) {
            throw new WebApplicationException("No se puede volver a proponer el mismo tema más de una vez", Response.Status.CONFLICT);
        }
    }
}

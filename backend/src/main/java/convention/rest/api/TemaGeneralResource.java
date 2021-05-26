package convention.rest.api;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import convention.persistent.TemaGeneral;
import convention.persistent.Usuario;
import convention.rest.api.tos.TemaGeneralTo;
import convention.services.TemaGeneralService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by sandro on 04/07/17.
 */
@Produces("application/json")
@Consumes("application/json")
public class TemaGeneralResource{

    @Inject
    private TemaGeneralService temaGeneralService;

    private ResourceHelper resourceHelper;

    private static final Type LISTA_DE_TEMAS_GENERALES_TO = new ReferenceOf<List<TemaGeneralTo>>() {
    }.getReferencedType();

    @GET
    public List<TemaGeneralTo> getAll() {
        List<TemaGeneral> temasGenerales = temaGeneralService.getAll();
        return getResourceHelper().convertir(temasGenerales, LISTA_DE_TEMAS_GENERALES_TO);

    }

    @POST
    public TemaGeneralTo create(TemaGeneralTo newState,@Context SecurityContext securityContext) {
        TemaGeneral temaCreado = getResourceHelper().convertir(newState, TemaGeneral.class);
        Usuario modificador = getResourceHelper().usuarioActual(securityContext);
        temaCreado.setUltimoModificador(modificador);
        return temaGeneralService.saving(temaCreado).convertTo(TemaGeneralTo.class);
    }

    @DELETE
    @Path("/{resourceId}")
    public void delete(@PathParam("resourceId") Long id) {
        temaGeneralService.delete(id);
    }

    @PUT
    @Path("/{resourceId}")
    public TemaGeneralTo update(TemaGeneralTo newState, @PathParam("resourceId") Long id,@Context SecurityContext securityContext) {
        TemaGeneral temaActualizado =getResourceHelper().convertir(newState, TemaGeneral.class);
        Usuario modificador=getResourceHelper().usuarioActual(securityContext);
        temaActualizado.setUltimoModificador(modificador);
        return temaGeneralService.saving(temaActualizado).convertTo(TemaGeneralTo.class);
    }

    public static TemaGeneralResource create(DependencyInjector appInjector) {
        TemaGeneralResource temaGeneralResource = new TemaGeneralResource();
        temaGeneralResource.resourceHelper=ResourceHelper.create(appInjector);
        temaGeneralResource.temaGeneralService = appInjector.createInjected(TemaGeneralService.class);
        temaGeneralResource.getResourceHelper().bindAppInjectorTo(TemaGeneralResource.class,temaGeneralResource);
        return temaGeneralResource;
    }

    public ResourceHelper getResourceHelper() {
        return resourceHelper;
    }
}

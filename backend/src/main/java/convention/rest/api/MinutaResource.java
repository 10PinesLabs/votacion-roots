package convention.rest.api;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import com.google.inject.Inject;
import convention.persistent.Minuta;
import convention.persistent.Usuario;
import convention.rest.api.tos.MinutaTo;
import convention.services.MinutaService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by sandro on 07/07/17.
 */
@Produces("application/json")
@Consumes("application/json")
public class MinutaResource{

    @Inject
    private MinutaService minutaService;

    private ResourceHelper resourceHelper;
    @GET
    @Path("reunion/{reunionId}")
    public MinutaTo getParaReunion(@PathParam("reunionId") Long id ){
        Minuta minuta = minutaService.getFromReunion(id);
        minutaService.update(minuta);
        return getResourceHelper().convertir(minuta, MinutaTo.class);
    }

    @PUT
    @Path("/{resourceId}")
    public MinutaTo update(MinutaTo newState, @PathParam("resourceId") Long id, @Context SecurityContext securityContext){
        Usuario ultimoMinuteador = this. getResourceHelper().usuarioActual(securityContext);
        Minuta minuta= getResourceHelper().convertir(newState, Minuta.class);
        minuta.setMinuteador(ultimoMinuteador);
        Minuta minutaActualizada = minutaService.update(minuta);
        return  getResourceHelper().convertir(minutaActualizada, MinutaTo.class);
    }

    @GET
    @Path("/ultimaMinuta")
    public MinutaTo getUltimaMinuta() {
        return minutaService.getUltimaMinuta()
                .map(minuta -> getResourceHelper().convertir(minuta, MinutaTo.class))
                .orElseThrow(() -> new WebApplicationException("La minuta no existe", Response.Status.NOT_FOUND));
    }

    public static MinutaResource create(DependencyInjector appInjector) {
        MinutaResource minutaResource = new MinutaResource();
        minutaResource.resourceHelper= ResourceHelper.create(appInjector);
        minutaResource. getResourceHelper().bindAppInjectorTo(MinutaResource.class, minutaResource);
        minutaResource.minutaService=appInjector.createInjected(MinutaService.class);
        return minutaResource;
    }

    public ResourceHelper getResourceHelper() {
        return resourceHelper;
    }
}

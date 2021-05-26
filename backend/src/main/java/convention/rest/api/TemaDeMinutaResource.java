package convention.rest.api;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import convention.persistent.Minuta;
import convention.persistent.TemaDeMinuta;
import convention.persistent.Usuario;
import convention.rest.api.tos.TemaDeMinutaTo;
import convention.services.MinutaService;
import convention.services.Service;
import convention.services.TemaDeMinutaService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by fede on 07/07/17.
 */

@Produces("application/json")
@Consumes("application/json")
public class TemaDeMinutaResource{

    @Inject
    private TemaDeMinutaService temaDeMinutaService;
    @Inject
    private MinutaService minutaService;

    private ResourceHelper resourceHelper;

    @GET
    @Path("/{resourceId}")
    public TemaDeMinutaTo getSingle(@PathParam("resourceId") Long id) {
        return temaDeMinutaService.getting(id).convertTo(TemaDeMinutaTo.class);
    }
    @PUT
    @Path("/{resourceId}")
    public TemaDeMinutaTo update(TemaDeMinutaTo newState, @PathParam("resourceId") Long id, @Context SecurityContext securityContext) {
        TemaDeMinuta temaDeMinuta = temaDeMinutaService.update(getResourceHelper().convertir(newState, TemaDeMinuta.class));
        Minuta minutaDelTema = minutaService.get(temaDeMinuta.getMinuta().getId());
        minutaDelTema.setMinuteador(getResourceHelper().usuarioActual(securityContext));
        minutaService.update(minutaDelTema);
        return temaDeMinutaService.getting(id).convertTo(TemaDeMinutaTo.class);
    }
    public static TemaDeMinutaResource create(DependencyInjector appInjector) {
        TemaDeMinutaResource temaDeMinutaResource = new TemaDeMinutaResource();
        temaDeMinutaResource.resourceHelper= ResourceHelper.create(appInjector);
        temaDeMinutaResource.temaDeMinutaService=appInjector.createInjected(TemaDeMinutaService.class);
        temaDeMinutaResource. getResourceHelper().bindAppInjectorTo(TemaDeMinutaResource.class, temaDeMinutaResource);
        return temaDeMinutaResource;
    }

    public ResourceHelper getResourceHelper() {
        return resourceHelper;
    }
}

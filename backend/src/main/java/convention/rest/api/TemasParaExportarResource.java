package convention.rest.api;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.temas.annotations.PATCH;
import ar.com.kfgodel.temas.config.environments.Environment;
import convention.persistent.Minuta;
import convention.persistent.TemaDeReunion;
import convention.rest.api.tos.TemaDeMinutaTo;
import convention.rest.api.tos.TemaDeReunionTo;
import convention.services.MinutaService;
import convention.services.ReunionService;
import convention.services.TemaDeMinutaService;
import convention.services.TemaService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Produces("application/json")
@Consumes("application/json")

public class TemasParaExportarResource {

    private static final String API_KEY_QUERY_PARAM = "apiKey";
    @Inject
    private ResourceHelper resourceHelper;
    @Inject
    private ReunionService reunionService;
    @Inject
    private TemaService temaService;
    @Inject
    private MinutaService minutaService;
    @Inject
    private TemaDeMinutaService temaDeMinutaService;

    public static TemasParaExportarResource create(DependencyInjector injector) {
        TemasParaExportarResource temasParaExportarResource = new TemasParaExportarResource();
        temasParaExportarResource.resourceHelper = ResourceHelper.create(injector);
        temasParaExportarResource.getResourceHelper().bindAppInjectorTo(TemasParaExportarResource.class, temasParaExportarResource);
        temasParaExportarResource.reunionService = injector.createInjected(ReunionService.class);
        return temasParaExportarResource;
    }

    @GET
    public List<TemaDeReunionTo> getProximosTemas(@QueryParam(API_KEY_QUERY_PARAM) String apiKey) {
        ensureAuthorized(apiKey);

        return reunionService
            .getTemasDeProximaReunion().stream()
            .map(tema -> getResourceHelper().convertir(tema, TemaDeReunionTo.class))
            .collect(Collectors.toList());
    }

    @PATCH
    @Path("/{resourceId}/temaDeMinuta")
    public void patchTemaDeMinuta(
        @QueryParam(API_KEY_QUERY_PARAM) String apiKey,
        @PathParam("resourceId") Long id, TemaDeMinutaTo patchRequest) {

        ensureAuthorized(apiKey);

        Boolean fueTratado = Optional.ofNullable(patchRequest.getFueTratado())
            .orElseThrow(() -> new WebApplicationException("El request es inválido", Response.Status.BAD_REQUEST));
        TemaDeReunion temaDeReunion = temaService.get(id);
        Minuta minuta = minutaService.getForReunion(temaDeReunion.getReunion().getId())
            .orElseThrow(() -> new WebApplicationException("La reunión no tiene minuta", Response.Status.NOT_FOUND));
        minuta.getTemas().stream()
            .filter(tema -> tema.getTema().equals(temaDeReunion))
            .findFirst()
            .ifPresent(temaDeMinuta -> {
                temaDeMinuta.setFueTratado(fueTratado);
                temaDeMinutaService.update(temaDeMinuta);
            });
    }

    private void ensureAuthorized(String apiKey) {
        if (!isAuthorized(apiKey)) {
            throw new WebApplicationException("Invalid or missing apikey", Response.Status.UNAUTHORIZED);
        }
    }

    public ResourceHelper getResourceHelper() {
        return resourceHelper;
    }


    public boolean isAuthorized(String apiKey) {
        return Environment.toHandle(System.getenv("ENVIROMENT")).apiKey().equals(apiKey);
    }
}

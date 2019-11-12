package convention.rest.api;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.temas.config.environments.Environment;
import convention.rest.api.tos.TemaDeReunionTo;
import convention.services.ReunionService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.stream.Collectors;

@Produces("application/json")
@Consumes("application/json")

public class TemasParaExportarResource {

    @Inject
    private ResourceHelper resourceHelper;

    @Inject
    private ReunionService reunionService;

    @GET
    public List<TemaDeReunionTo> getProximosTemas(@QueryParam("apiKey") String apiKey) {
        if (!isAuthorized(apiKey)) throw new RuntimeException("Invalid or missing apikey");

        return reunionService
                .getTemasDeProximaReunion().stream()
                .map(tema -> getResourceHelper().convertir(tema, TemaDeReunionTo.class))
                .collect(Collectors.toList());
    }

    public static TemasParaExportarResource create(DependencyInjector injector) {
        TemasParaExportarResource temasParaExportarResource = new TemasParaExportarResource();
        temasParaExportarResource.resourceHelper = ResourceHelper.create(injector);
        temasParaExportarResource.getResourceHelper().bindAppInjectorTo(TemasParaExportarResource.class, temasParaExportarResource);
        temasParaExportarResource.reunionService = injector.createInjected(ReunionService.class);
        return temasParaExportarResource;
    }

    public ResourceHelper getResourceHelper() {
        return resourceHelper;
    }


    public boolean isAuthorized(String apiKey) {
        return Environment.toHandle(System.getenv("ENVIROMENT")).apiKey().equals(apiKey);
    }
}

package convention.rest.api;

import ar.com.kfgodel.temas.application.Application;

import javax.inject.Inject;
import javax.ws.rs.Path;

/**
 * This type represents the root resource for v1 api access.<br>
 * This allows multiple versions of the api in the same server (if needed)
 * *
 * Created by kfgodel on 03/03/15.
 */
@Path("/api/v2")
public class ApiV2Public {

  // Injected by jetty/jersey H2k internal binder (which only knwons the application)
  @Inject
  private Application application;

  private TemasParaExportarResource temasParaExportar;

  @Path("/temas")
  public TemasParaExportarResource temasParaExportar() {
    if (temasParaExportar == null) {
      temasParaExportar = TemasParaExportarResource.create(application.injector());
    }
    return temasParaExportar;
  }

}

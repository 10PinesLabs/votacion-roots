package convention.rest.api;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import convention.persistent.DuracionDeTema;
import convention.rest.api.tos.DuracionDeTemaTo;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by fede on 22/06/17.
 */

@Produces("application/json")
@Consumes("application/json")

public class DuracionesResource {

    @Inject
    private ResourceHelper resourceHelper;

    public List<DuracionDeTema> getAllDuraciones(){
            List<DuracionDeTema> listaOrdenada=Arrays.asList(DuracionDeTema.values());
            listaOrdenada.sort(Comparator.comparingInt(DuracionDeTema::getCantidadDeMinutos));
            return   getResourceHelper().createOperation().
                    insideASession()
                    .taking(listaOrdenada)
                    .convertTo(new ReferenceOf<List<DuracionDeTema>>(){}.getReferencedType());

    }
    @GET
    public List<DuracionDeTemaTo> getAll() {
         return  getResourceHelper().convertir(getAllDuraciones(), new ReferenceOf<List<DuracionDeTemaTo>>(){}.getReferencedType());
    }

   public  static  DuracionesResource create(DependencyInjector appInjector){
        DuracionesResource duracionesResource=new DuracionesResource();
        duracionesResource.resourceHelper= ResourceHelper.create(appInjector);
        duracionesResource.getResourceHelper().bindAppInjectorTo(DuracionesResource.class,duracionesResource);
        return duracionesResource;
    }


    public ResourceHelper getResourceHelper() {
        return resourceHelper;
    }
}

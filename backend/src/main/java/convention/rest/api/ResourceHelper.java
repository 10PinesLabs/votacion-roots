package convention.rest.api;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.orm.api.operations.basic.FindById;
import ar.com.kfgodel.temas.exceptions.TypeTransformerException;
import ar.com.kfgodel.transformbyconvention.api.TypeTransformer;
import ar.com.kfgodel.webbyconvention.impl.auth.adapters.JettyIdentityAdapter;
import convention.persistent.Usuario;

import javax.inject.Inject;
import javax.ws.rs.core.SecurityContext;
import java.lang.reflect.Type;

/**
 * Created by fede on 27/06/17.
 */
public  class ResourceHelper {


    @Inject
    private DependencyInjector appInjector;

    public Long idDeUsuarioActual(SecurityContext securityContext){
        return ((JettyIdentityAdapter) securityContext.getUserPrincipal()).getApplicationIdentification();

    }
    static ResourceHelper create(DependencyInjector appInjector){
        ResourceHelper resourceHelper=new ResourceHelper();
        resourceHelper.appInjector=appInjector;
        return resourceHelper;
    }

    public <T> void bindAppInjectorTo(Class<T> klass,T t){
        appInjector.bindTo(klass,t);
    }
    public ApplicationOperation createOperation() {

        return ApplicationOperation.createFor(appInjector);
    }

    public Usuario usuarioActual(SecurityContext securityContext){
        Long userId=idDeUsuarioActual(securityContext);
        return createOperation()
                .insideASession()
                .applying(FindById.create(Usuario.class, userId))
                .mapping((encontrado) ->
                        encontrado.orElse(null))
                .get();
    }

    public <T> T convertir(Object objetoAConvertir, Class<T> claseHacia){
        return (T) appInjector.getImplementationFor(TypeTransformer.class).orElseThrow(() -> new TypeTransformerException("no se ha injectado ningun typeTransformer"))
                .transformTo(claseHacia,objetoAConvertir);
    }
    public <T> T convertir(Object objetoAConvertir, Type claseHacia){
        return (T) appInjector.getImplementationFor(TypeTransformer.class).orElseThrow(() -> new TypeTransformerException("no se ha injectado ningun typeTransformer"))
                .transformTo(claseHacia,objetoAConvertir);
    }

}

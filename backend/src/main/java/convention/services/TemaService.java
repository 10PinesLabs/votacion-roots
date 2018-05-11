package convention.services;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.orm.api.operations.basic.DeleteById;
import ar.com.kfgodel.orm.api.operations.basic.FindAll;
import ar.com.kfgodel.orm.api.operations.basic.FindById;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import convention.persistent.StatusDeReunion;
import convention.persistent.TemaDeMinuta;
import convention.persistent.TemaDeReunion;
import convention.persistent.TemaGeneral;
import convention.rest.api.tos.TemaTo;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by sandro on 21/06/17.
 */
public class TemaService extends Service<TemaDeReunion> {


    public TemaService(){
        setClasePrincipal(TemaDeReunion.class);
    }

    public List<TemaDeReunion> getAll() {
        return getAll(FindAll.of(TemaDeReunion.class));
    }

    @Override
    public void delete(Long id) {
       createOperation()
                .insideATransaction()
                .apply(
                        DeleteById.create(TemaDeReunion.class, id));
    }

    public void deleteAllForTemaGeneral(Long temaGeneralId) {
        List<TemaDeReunion> todosLosTemas = this.getAll();
        Stream<TemaDeReunion> temasABorrar = todosLosTemas.stream().
                filter(tema -> this.esUnTemaGeneradoPorElTemaGeneral(tema, temaGeneralId));
        temasABorrar.forEach(tema -> this.deleteSiReunionPendiente(tema));
    }

    public void updateAllForTemaGeneral(TemaGeneral temaModificado) {
        List<TemaDeReunion> todosLosTemas = this.getAll();
        Stream<TemaDeReunion> temasGeneradosYNoModificados = todosLosTemas.stream().
                filter(tema -> this.esUnTemaGeneradoPorElTemaGeneral(tema, temaModificado.getId()) && !tema.fueModificado());
        temasGeneradosYNoModificados.forEach(tema -> this.updateSiReunionPendiente(tema, temaModificado));
    }

    private Boolean esUnTemaGeneradoPorElTemaGeneral(TemaDeReunion tema, Long temaGeneralId){
        return tema.getTemaGenerador().isPresent() &&
                temaGeneralId.equals(tema.getTemaGenerador().get().getId());
    }

    private void deleteSiReunionPendiente(TemaDeReunion tema) {
        if(tema.getReunion().getStatus().equals(StatusDeReunion.PENDIENTE)){
            this.delete(tema.getId());
        }else{
            tema.setTemaGenerador(null);
            this.update(tema);
        }
    }

    private void updateSiReunionPendiente(TemaDeReunion tema, TemaGeneral temaModificado) {
        if(tema.getReunion().getStatus().equals(StatusDeReunion.PENDIENTE)){
            temaModificado.actualizarTema(tema);
            this.update(tema);
        }
    }
}

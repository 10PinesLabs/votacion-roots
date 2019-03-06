package convention.services;

import ar.com.kfgodel.orm.api.operations.basic.FindAll;
import ar.com.kfgodel.temas.acciones.CrearMinuta;
import ar.com.kfgodel.temas.acciones.UsarMinutaExistente;
import ar.com.kfgodel.temas.filters.MinutaDeReunion;
import convention.persistent.Minuta;
import convention.persistent.Reunion;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by sandro on 07/07/17.
 */
public class MinutaService extends Service<Minuta> {

    @Inject
    ReunionService reunionService;

    public MinutaService(){
        setClasePrincipal(Minuta.class);
    }

    public Minuta getFromReunion(Long id){

        return createOperation()
                .insideATransaction()
                .applying(MinutaDeReunion.create(id))
                .applyingResultOf((existente) ->
                        existente.mapOptional(UsarMinutaExistente::create)
                                .orElseGet(() ->{   Reunion reunion = reunionService.get(id);
                                             reunion.marcarComoMinuteada();
                                             reunionService.update(reunion);
                                        return CrearMinuta.create(reunion);
                                })
                                ).get();

    }

    public List<Minuta> getAll() {
        return getAll(FindAll.of(Minuta.class));
    }

    public Minuta getUltimaMinuta() {
      return this.getFromReunion(reunionService.getUltimaReunion().getId());
    }
}

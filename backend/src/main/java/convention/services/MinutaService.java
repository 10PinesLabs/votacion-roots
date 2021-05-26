package convention.services;

import ar.com.kfgodel.appbyconvention.operation.api.chains.ChainedSessionOperation;
import ar.com.kfgodel.appbyconvention.operation.api.chains.ChainedTransactionOperation;
import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.orm.api.operations.basic.FindAll;
import ar.com.kfgodel.temas.acciones.CrearMinuta;
import ar.com.kfgodel.temas.acciones.UsarMinutaExistente;
import ar.com.kfgodel.temas.filters.MinutaDeReunion;
import convention.persistent.Minuta;
import convention.persistent.Reunion;
import convention.persistent.Usuario;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Created by sandro on 07/07/17.
 */
public class MinutaService extends Service<Minuta> {

    @Inject
    ReunionService reunionService;

    public MinutaService() {
        setClasePrincipal(Minuta.class);
    }

    public ChainedTransactionOperation<Minuta> getOrCreateForReunion(Long id, Usuario unMinuteador) {

        return createOperation()
            .insideATransaction()
            .applying(MinutaDeReunion.create(id))
            .applyingResultOf((existente) ->
                existente.mapOptional(UsarMinutaExistente::create)
                    .orElseGet(() -> {
                        Reunion reunion = reunionService.get(id);
                        reunion.marcarComoMinuteada();
                        reunionService.update(reunion);
                        return CrearMinuta.create(reunion, unMinuteador);
                    })
            );
    }

    public Optional<Minuta> getForReunion(Long id) {
        return createOperation()
                .insideATransaction()
                .apply(MinutaDeReunion.create(id))
                .asOptional()
                .asNativeOptional();
    }

    public List<Minuta> getAll() {
        return getAll(FindAll.of(Minuta.class));
    }

    public Optional<Minuta> getUltimaMinuta() {
        return gettingUltimaMinuta().get();
    }

    public ChainedSessionOperation<Optional<Minuta>> gettingUltimaMinuta() {
        return reunionService.getUltimaReunion()
            .applyingResultOf(MinutaDeReunion::create)
            .mapping(Nary::asNativeOptional);
    }
}

package convention.services;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.appbyconvention.operation.api.chains.ChainedSessionOperation;
import ar.com.kfgodel.appbyconvention.operation.api.chains.ChainedTransactionOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.orm.api.operations.SessionOperation;
import ar.com.kfgodel.orm.api.operations.basic.Delete;
import ar.com.kfgodel.orm.api.operations.basic.FindById;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import convention.persistent.PersistableSupport;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * Created by fede on 28/06/17.
 */
public abstract class Service<T extends PersistableSupport> {

    @Inject
    protected DependencyInjector appInjector;

    protected Type LIST_TYPE = new ReferenceOf<List<T>>() {}.getReferencedType();
    protected Class<T> clasePrincipal;

    public List<T> getAll(SessionOperation<Nary<T>> sessionOperation) {
        return gettingAll(sessionOperation).get();
    }

    public ChainedSessionOperation<List<T>> gettingAll(SessionOperation<Nary<T>> sessionOperation) {
        return createOperation()
                .insideASession()
                .applying(sessionOperation)
                .convertingTo(LIST_TYPE);
    }

    public Type getList_Type() {
        return LIST_TYPE;
    }

    protected ApplicationOperation createOperation() {
        return ApplicationOperation.createFor(appInjector);
    }


    public T save(T newObject) {
        return saving(newObject).get();
    }

    public ChainedTransactionOperation<T> saving(T newObject) {
        return createOperation()
                .insideATransaction()
                .taking(newObject)
                .applyingResultOf(Save::create)
                .convertingTo(clasePrincipal);
    }

    public T get(Long id) {
        return getting(id).get();
    }

    public ChainedSessionOperation<T> getting(Long id) {
        return createOperation()
                .insideASession()
                .applying(FindById.create(clasePrincipal, id))
                .mapping(this::controlDeTargetAndReturn)
                .convertingTo(clasePrincipal);
    }

    public T getAndMapping(Long id, Function<T, T> mapping) {
        return createOperation()
                .insideASession()
                .applying(FindById.create(clasePrincipal, id))
                .mapping((encontrado) -> mapping.apply(controlDeTargetAndReturn(encontrado)))
                .convertTo(clasePrincipal);
    }

    public T updateAndMapping(Long id, Function<T, T> mapping) {
        return updatingAndMapping(id, mapping).get();
    }

    public ChainedTransactionOperation<T> updatingAndMapping(Long id, Function<T, T> mapping) {
        return createOperation()
                .insideATransaction()
                .applying((context) -> FindById.create(clasePrincipal, id).applyWithSessionOn(context))
                .mapping((encontrado) -> mapping.apply(controlDeTargetAndReturn(encontrado)))
                .applyingResultOf(Save::create);
    }

    public T update(T newState) {
        return updating(newState).get();
    }

    public ChainedTransactionOperation<T> updating(T newState) {
        return createOperation()
                .insideATransaction()
                .taking(newState)
                .convertingTo(clasePrincipal)
                .mapping((encontrado) -> {
                    // Answer 404 if missing
                    controlDeTarget(encontrado);
                    return encontrado;
                }).applyingResultOf(Save::create);
    }

    public void delete(Long id) {

        createOperation()
                .insideATransaction()
                .taking(id)
                .convertingTo(clasePrincipal)
                .mapping((encontrado) -> {
                    controlDeTarget(encontrado);
                    return encontrado;
                })
                .applyResultOf(Delete::create);
    }

    private void controlDeTarget(T encontrado) {
        if (encontrado == null) {
            throw new WebApplicationException("target not found", Response.Status.NOT_FOUND);
        }
    }

    private T controlDeTargetAndReturn(Nary<T> encontrado) {
        return encontrado.orElseThrow(() -> new WebApplicationException("target not found", Response.Status.NOT_FOUND));
    }

    protected void setClasePrincipal(Class<T> clasePrincipal) {
        this.clasePrincipal = clasePrincipal;
    }

    public void saveAll(List<T> newObjects) {
        newObjects.forEach(newObject -> this.save(newObject));
    }

    public void updateAll(List<T> newObjects) {
        newObjects.forEach(newObject -> this.update(newObject));
    }

    public void delete(T anObject) {
        createOperation()
                .insideATransaction()
                .apply(Delete.create(anObject));
    }

    public void deleteAll() {
        getAll().forEach(anObject -> delete(anObject));
    }

    protected abstract Collection<T> getAll();
}

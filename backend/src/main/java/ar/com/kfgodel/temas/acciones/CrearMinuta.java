package ar.com.kfgodel.temas.acciones;

import ar.com.kfgodel.orm.api.TransactionContext;
import ar.com.kfgodel.orm.api.operations.TransactionOperation;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import convention.persistent.Minuta;
import convention.persistent.Reunion;
import convention.persistent.Usuario;

/**
 * Created by sandro on 07/07/17.
 */
public class CrearMinuta implements TransactionOperation<Minuta> {

    private Reunion reunion;
    private Usuario minuteador;

    public static CrearMinuta create(Reunion reunion, Usuario unMinuteador){
        CrearMinuta accion = new CrearMinuta();
        accion.reunion = reunion;
        accion.minuteador = unMinuteador;
        return accion;
    }

    public static CrearMinuta create(){
        CrearMinuta accion = new CrearMinuta();
        return accion;
    }

    @Override
    public Minuta applyWithTransactionOn(TransactionContext transactionContext) {
        Minuta nuevaMinuta = Minuta.create(reunion);
        nuevaMinuta.setMinuteador(minuteador);
        Save.create(nuevaMinuta).applyWithTransactionOn(transactionContext);
        return nuevaMinuta;
    }
}

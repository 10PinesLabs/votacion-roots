package ar.com.kfgodel.temas.acciones;

import ar.com.kfgodel.orm.api.TransactionContext;
import ar.com.kfgodel.orm.api.operations.TransactionOperation;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import convention.persistent.QTemaDeReunion;

public class ConvertirRePropuestasAPrimerasPropuestas implements TransactionOperation {
    private Long idDePrimeraPropuesta;

    public static ConvertirRePropuestasAPrimerasPropuestas create(Long id) {
        ConvertirRePropuestasAPrimerasPropuestas accion = new ConvertirRePropuestasAPrimerasPropuestas();
        accion.idDePrimeraPropuesta = id;
        return accion;
    }

    @Override
    public Object applyWithTransactionOn(TransactionContext transactionContext) {
        QTemaDeReunion temaDeReunion = QTemaDeReunion.temaDeReunion;
        new HibernateQueryFactory(transactionContext.getSession())
                .update(temaDeReunion)
                .where(temaDeReunion.primeraPropuesta.id.eq(idDePrimeraPropuesta))
                .setNull(temaDeReunion.primeraPropuesta)
                .execute();
        return null;
    }
}

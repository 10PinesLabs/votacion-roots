package ar.com.kfgodel.temas.acciones;

import ar.com.kfgodel.orm.api.TransactionContext;
import ar.com.kfgodel.orm.api.operations.TransactionOperation;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import convention.persistent.QTemaDeReunion;

public class ConvertirRePropuestasAPropuestasOriginales implements TransactionOperation {
    private Long idDePropuestaOriginal;

    public static ConvertirRePropuestasAPropuestasOriginales create(Long id) {
        ConvertirRePropuestasAPropuestasOriginales accion = new ConvertirRePropuestasAPropuestasOriginales();
        accion.idDePropuestaOriginal = id;
        return accion;
    }

    @Override
    public Object applyWithTransactionOn(TransactionContext transactionContext) {
        QTemaDeReunion temaDeReunion = QTemaDeReunion.temaDeReunion;
        new HibernateQueryFactory(transactionContext.getSession())
                .update(temaDeReunion)
                .where(temaDeReunion.propuestaOriginal.id.eq(idDePropuestaOriginal))
                .setNull(temaDeReunion.propuestaOriginal)
                .execute();
        return null;
    }
}

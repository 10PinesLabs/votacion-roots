package ar.com.kfgodel.temas.filters;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.orm.api.TransactionContext;
import ar.com.kfgodel.orm.api.operations.TransactionOperation;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import convention.persistent.Minuta;
import convention.persistent.QMinuta;
import convention.persistent.Reunion;

import java.util.Optional;

/**
 * Created by sandro on 07/07/17.
 */
public class MinutaDeReunion implements TransactionOperation<Nary<Minuta>> {

    private final Long reunionId;

    public MinutaDeReunion(Long reunionId) {
        this.reunionId = reunionId;
    }

    public static MinutaDeReunion create(Optional<Reunion> reunionOptional) {
        return new MinutaDeReunion(reunionOptional.map(Reunion::getId).orElse(null));
    }

    public static MinutaDeReunion create(Long reunionId) {
        return new MinutaDeReunion(reunionId);
    }

    @Override
    public Nary<Minuta> applyWithTransactionOn(TransactionContext transactionContext) {
        if (reunionId == null) {
            return Nary.empty();
        }

        HibernateQueryFactory queryFactory = new HibernateQueryFactory(transactionContext.getSession());

        QMinuta minuta = QMinuta.minuta;

        Minuta minutaDeReunion = queryFactory.selectFrom(minuta)
            .where(minuta.reunion.id.eq(reunionId))
            .fetchOne();
        return Nary.ofNullable(minutaDeReunion);
    }
}

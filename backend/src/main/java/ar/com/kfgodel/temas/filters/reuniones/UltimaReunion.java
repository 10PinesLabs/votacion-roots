package ar.com.kfgodel.temas.filters.reuniones;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.orm.api.TransactionContext;
import ar.com.kfgodel.orm.api.operations.TransactionOperation;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import convention.persistent.QReunion;
import convention.persistent.Reunion;
import convention.persistent.StatusDeReunion;

public class UltimaReunion implements TransactionOperation<Nary<Reunion>> {

  public static UltimaReunion create() {
    UltimaReunion filtro = new UltimaReunion();
    return filtro;
  }

  @Override
  public Nary<Reunion> applyWithTransactionOn(TransactionContext transactionContext) {
    HibernateQueryFactory queryFactory = new HibernateQueryFactory(transactionContext.getSession());

    QReunion reunion = QReunion.reunion;

    Reunion ultimaReunion = queryFactory.selectFrom(reunion)
      .where(reunion.status.eq(StatusDeReunion.CON_MINUTA))
      .orderBy(reunion.fecha.desc())
      .limit(1)
      .fetchOne();

    return Nary.ofNullable(ultimaReunion);
  }
}

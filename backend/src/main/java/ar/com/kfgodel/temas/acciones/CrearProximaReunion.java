package ar.com.kfgodel.temas.acciones;

import ar.com.kfgodel.orm.api.TransactionContext;
import ar.com.kfgodel.orm.api.operations.TransactionOperation;
import convention.persistent.Reunion;
import convention.services.ReunionService;

import java.time.LocalDate;

/**
 * Esta clase representa el creador de reunion de proxima roots, que sabe calcular la fecha de la proxima
 * Created by kfgodel on 08/10/16.
 */
public class CrearProximaReunion implements TransactionOperation<Reunion> {

  private ReunionService reunionService;

  @Override
  public Reunion applyWithTransactionOn(TransactionContext transactionContext) {
    LocalDate fechaDeProximaRoots = new CalculadorDeProximaFecha().calcularFechaDeRoots(LocalDate.now());
    Reunion proximaRoots = Reunion.create(fechaDeProximaRoots);
    proximaRoots = reunionService.create(proximaRoots);
    return proximaRoots;
  }

  public void setReunionService(ReunionService reunionService) {
    this.reunionService = reunionService;
  }

  public static CrearProximaReunion create(ReunionService reunionService) {
    CrearProximaReunion accion = new CrearProximaReunion();
    accion.setReunionService(reunionService);
    return accion;
  }

}

package ar.com.kfgodel.temas.apiRest;

import convention.persistent.Reunion;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class ReunionResourceTest extends ResourcesTemasTest {

  private Reunion reunionConMinutaDel24;
  private Reunion reunionConMinutaDel22;
  private Reunion reunionConMinutaDel23;
  private Reunion reunionPendienteDel27;
  private Reunion reunionCerradaDel26;

  @Override
  public void setUp() {
    super.setUp();
    reunionCerradaDel26 = Reunion.create(LocalDate.of(2018, 12, 26));
    reunionCerradaDel26.cerrarVotacion();
    reunionCerradaDel26 = reunionService.save(reunionCerradaDel26);

    reunionPendienteDel27 = reunionService.save(Reunion.create(LocalDate.of(2018, 12, 27)));

    reunionConMinutaDel22 = crearReunionMinuteadaDe(LocalDate.of(2018, 12, 22));

    reunionConMinutaDel24 = crearReunionMinuteadaDe(LocalDate.of(2018, 12, 24));

    reunionConMinutaDel23 = crearReunionMinuteadaDe(LocalDate.of(2018, 12, 23));
  }

  private Reunion crearReunionMinuteadaDe(LocalDate fecha) {
    Reunion reunion = Reunion.create(fecha);
    reunion.marcarComoMinuteada();
    return reunionService.save(reunion);
  }

  @Test
  public void puedoPedirLaUltimaReunionCerrada() {
    assertThat(reunionService.getUltimaReunion().getId()).isEqualTo(reunionConMinutaDel24.getId());
  }
}

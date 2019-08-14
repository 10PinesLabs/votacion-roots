package ar.com.kfgodel.temas.services;

import convention.persistent.Reunion;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class ReunionServiceTest extends TemasServiceTest {

    private Reunion reunionConMinutaDel24;

    @Override
    public void setUp() {
        super.setUp();
        reunionConMinutaDel24 = crearReunionMinuteadaDe(LocalDate.of(2018, 12, 24));
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

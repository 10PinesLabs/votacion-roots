package ar.com.kfgodel.temas.tos;

import ar.com.kfgodel.temas.application.Application;
import ar.com.kfgodel.temas.exceptions.TypeTransformerException;
import ar.com.kfgodel.temas.helpers.TestConfig;
import ar.com.kfgodel.temas.helpers.TestHelper;
import ar.com.kfgodel.temas.persistence.TestApplication;
import ar.com.kfgodel.transformbyconvention.api.TypeTransformer;
import convention.persistent.PropuestaDePinoARoot;
import convention.persistent.Usuario;
import convention.rest.api.tos.PropuestaDePinoARootTo;
import convention.rest.api.tos.UserTo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PropuestaDePinoARootToTest {

    private TestHelper helper = new TestHelper();
    private Application application;
    private TypeTransformer baseConverter;

    @Before
    public void setUp() {
        application = TestApplication.create(TestConfig.create());
        application.start();
        baseConverter = application.injector().getImplementationFor(TypeTransformer.class)
                .orElseThrow(() -> new TypeTransformerException("no se ha injectado ningun typeTransformer"));
    }

    @After
    public void tearDown() {
        application.stop();
    }

    @Test
    public void testSePuedeConvertirDePropuestaATo() {
        PropuestaDePinoARoot unaPropuesta = helper.unaPropuestaDeUnPinoARoot();

        PropuestaDePinoARootTo propuestaTo = baseConverter.transformTo(PropuestaDePinoARootTo.class, unaPropuesta);
        UserTo sponsorTo = baseConverter.transformTo(UserTo.class, unaPropuesta.sponsor());

        assertThat(propuestaTo.getPino()).isEqualTo(unaPropuesta.pino());
        assertThat(propuestaTo.getSponsor().getBackofficeId()).isEqualTo(sponsorTo.getBackofficeId());
    }

    @Test
    public void testSePuedeConvertirDeToAPropuesta() {
        PropuestaDePinoARootTo unPropuestaTo = new PropuestaDePinoARootTo(helper.unPino(), helper.unUserTo());

        PropuestaDePinoARoot propuesta = baseConverter.transformTo(PropuestaDePinoARoot.class, unPropuestaTo);
        Usuario sponsor = baseConverter.transformTo(Usuario.class, unPropuestaTo.getSponsor());

        assertThat(propuesta.pino()).isEqualTo(unPropuestaTo.getPino());
        assertThat(propuesta.sponsor().getBackofficeId()).isEqualTo(sponsor.getBackofficeId());
    }

}

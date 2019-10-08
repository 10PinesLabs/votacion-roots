package ar.com.kfgodel.temas.domain;

import ar.com.kfgodel.temas.helpers.TestHelper;
import convention.persistent.PropuestaDePinoARoot;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class PropuestaDePinoARootTest {

    private TestHelper helper = new TestHelper();

    @Test
    public void testElPinoNoPuedeSerNull() {
        Assertions.assertThatNullPointerException().isThrownBy(() -> {
            new PropuestaDePinoARoot(null, helper.unUsuario());
        });
    }

    @Test
    public void testElUsuarioNoPuedeSerNull() {
        Assertions.assertThatNullPointerException().isThrownBy(() -> {
            new PropuestaDePinoARoot(helper.unPino(), null);
        });
    }
}

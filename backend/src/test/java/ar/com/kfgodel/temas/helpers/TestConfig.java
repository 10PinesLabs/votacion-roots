package ar.com.kfgodel.temas.helpers;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.orm.api.config.DbCoordinates;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import ar.com.kfgodel.orm.impl.config.ImmutableDbCoordinates;
import ar.com.kfgodel.temas.config.DevelopmentConfig;
import convention.persistent.Usuario;
import convention.rest.api.tos.UserTo;

/**
 * Created by sandro on 22/06/17.
 */

/**
 * Esta configuracion necesita inicializar minimamente 2 usuarios distintos para los test.
**/
public class TestConfig extends DevelopmentConfig {

    @Override
    public DbCoordinates getDatabaseCoordinates() {
        return ImmutableDbCoordinates.createDeductingDialect("jdbc:h2:mem:file:./db/testDB", "sa", "");
    }

    public static TestConfig create() {
        TestConfig config = new TestConfig();
        return config;
    }
    @Override
    public Void inicializarDatos(){
        if(getUsers().isEmpty()) {
            Usuario unUser = Usuario.create("feche", "fecheromero", "123", "sarlnga","mail@10pines.com");
            ApplicationOperation.createFor(getInjector())
                    .insideATransaction()
                    .taking(unUser)
                    .convertingTo(Usuario.class)
                    .applyingResultOf(Save::create)
                    .convertTo(UserTo.class);

            Usuario unUser2 = Usuario.create("sandro", "unSandro", "123", "sarlonga", "mail2@10pines.com");
            ApplicationOperation.createFor(getInjector())
                    .insideATransaction()
                    .taking(unUser2)
                    .convertingTo(Usuario.class)
                    .applyingResultOf(Save::create)
                    .convertTo(UserTo.class);
        }
        return null;
    }
}

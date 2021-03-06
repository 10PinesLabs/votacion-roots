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
        return ImmutableDbCoordinates.createDeductingDialect("jdbc:h2:mem:ludat;DB_CLOSE_DELAY=0", "sa", "");
    }

    public static TestConfig create() {
        TestConfig config = new TestConfig();
        return config;
    }
    @Override
    public Void inicializarDatos(){
        return null;
    }
}

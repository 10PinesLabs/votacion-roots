package ar.com.kfgodel.temas.config;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HerokuPriorityConfigSelector.class})
public class EnviromentConfigTest {

    private HerokuPriorityConfigSelector configSelector;
    private String variableName = "ENVIRONMENT";

    @Before
    public void setUp(){
        configSelector = HerokuPriorityConfigSelector.create();
        PowerMockito.mockStatic(System.class);
    }

    @Test
    public void siElEnviromentEsDevelopmentLaConfiguracionEsDevelopment(){
        PowerMockito.when(System.getenv(variableName)).thenReturn("DEV");
        assertThat(configSelector.selectConfig()).isInstanceOf(DevelopmentConfig.class);
    }

    @Test
    public void siElEnviromentEsProduccionLaConfiguracionEsHerokuConAccesoParaRoots(){
        PowerMockito.when(System.getenv(variableName)).thenReturn("PROD");
        assertThat(configSelector.selectConfig()).isInstanceOf(HerokuConfig.class);
    }

    @Test
    public void siElEnviromentEsStaginLaConfiguracionEsHerokuConAccesoParaTodos(){
        PowerMockito.when(System.getenv(variableName)).thenReturn("STG");
        assertThat(configSelector.selectConfig()).isInstanceOf(HerokuStagingConfig.class);
    }
}

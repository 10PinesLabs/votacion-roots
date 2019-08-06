package ar.com.kfgodel.temas.tos;

import ar.com.kfgodel.temas.application.Application;
import ar.com.kfgodel.temas.exceptions.TypeTransformerException;
import ar.com.kfgodel.temas.helpers.TestConfig;
import ar.com.kfgodel.temas.helpers.TestHelper;
import ar.com.kfgodel.temas.persistence.TestApplication;
import ar.com.kfgodel.transformbyconvention.api.TypeTransformer;
import convention.persistent.PersistableSupport;
import convention.persistent.PropuestaDePinoARoot;
import convention.persistent.TemaDeReunionConDescripcion;
import convention.persistent.TemaParaProponerPinosARoot;
import convention.rest.api.tos.PropuestaDePinoARootTo;
import convention.rest.api.tos.TemaDeReunionConDescripcionTo;
import convention.rest.api.tos.TemaDeReunionTo;
import convention.rest.api.tos.TemaParaProponerPinosARootTo;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class TemaDeReunionToTest {

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

    @Test
    public void testSePuedeConvertirAUnTemaDeReunionConDescripcionTo() {
        TemaDeReunionConDescripcion tema = helper.unTemaDeReunionConDescripcion();

        TemaDeReunionConDescripcionTo temaTo = (TemaDeReunionConDescripcionTo)
                baseConverter.transformTo(TemaDeReunionTo.class, tema);

        assertThat(temaTo.getIdDeAutor()).isEqualTo(tema.getAutor().getId());
        assertThat(temaTo.getTitulo()).isEqualTo(tema.getTitulo());
        assertThat(temaTo.getDescripcion()).isEqualTo(tema.getDescripcion());
        assertThat(temaTo.getDuracion()).isEqualTo(convertEnumToString(tema.getDuracion()));
        assertThat(temaTo.getObligatoriedad()).isEqualTo(convertEnumToString(tema.getObligatoriedad()));
    }

    @Test
    public void testSePuedeConvertirAUnTemaParaProponerPinosComoRootTo() {
        TemaParaProponerPinosARoot tema = helper.unTemaParaProponerPinosARoot();

        TemaParaProponerPinosARootTo temaTo = (TemaParaProponerPinosARootTo)
                baseConverter.transformTo(TemaDeReunionTo.class, tema);
        List<String> propuestasTo = tema.propuestas().stream().map(PropuestaDePinoARoot::pino).collect(Collectors.toList());

        assertThat(temaTo.getIdDeAutor()).isEqualTo(tema.getAutor().getId());
        assertThat(temaTo.getTitulo()).isEqualTo(tema.getTitulo());
        assertThat(temaTo.getDuracion()).isEqualTo(convertEnumToString(tema.getDuracion()));
        assertThat(temaTo.getObligatoriedad()).isEqualTo(convertEnumToString(tema.getObligatoriedad()));
        assertThat(temaTo.getPropuestas().stream().map(PropuestaDePinoARootTo::getPino)).containsExactlyElementsOf(propuestasTo);
    }

    private String convertEnumToString(Object enumValue) {
        return baseConverter.transformTo(String.class, enumValue);
    }

}

package ar.com.kfgodel.temas.tos;

import ar.com.kfgodel.temas.application.Application;
import ar.com.kfgodel.temas.exceptions.TypeTransformerException;
import ar.com.kfgodel.temas.helpers.TestConfig;
import ar.com.kfgodel.temas.helpers.TestHelper;
import ar.com.kfgodel.temas.persistence.TestApplication;
import ar.com.kfgodel.transformbyconvention.api.TypeTransformer;
import convention.persistent.*;
import convention.rest.api.tos.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
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

    @Test
    public void testSePuedeConvertirDeUnTemaDeReunionConDescripcionTo() {
        Usuario unAutor = helper.unUsuario();
        String unaDuracion = convertEnumToString(helper.unaDuracion());
        String unaObligatoriedad = convertEnumToString(helper.unaObligatoriedad());
        String unTitulo = helper.unTitulo();
        String unaDescripcion = helper.unaDescripcion();
        TemaDeReunionConDescripcionTo temaTo =
                TemaDeReunionConDescripcionTo.create(unAutor, unaDuracion, unaObligatoriedad, unTitulo, unaDescripcion);

        TemaDeReunionConDescripcion tema = (TemaDeReunionConDescripcion)
                baseConverter.transformTo(TemaDeReunion.class, temaTo);

        assertThat(tema.getTitulo()).isEqualTo(temaTo.getTitulo());
        assertThat(tema.getDescripcion()).isEqualTo(temaTo.getDescripcion());
        assertThat(convertEnumToString(tema.getDuracion())).isEqualTo(temaTo.getDuracion());
        assertThat(convertEnumToString(tema.getObligatoriedad())).isEqualTo(temaTo.getObligatoriedad());
    }

    @Test
    public void testSePuedeConvertirDeUnTemaParaProponerPinosComoRootTo() {
        Usuario unAutor = helper.unUsuario();
        String unaDuracion = convertEnumToString(TemaParaProponerPinosARoot.DURACION);
        String unaObligatoriedad = convertEnumToString(TemaParaProponerPinosARoot.OBLIGATORIEDAD);
        String unTitulo = TemaParaProponerPinosARoot.TITULO;
        List<PropuestaDePinoARootTo> unosPropuestasTo = new ArrayList<>();
        String unPino = helper.unPino();
        String otroPino = helper.otroPino();
        UserTo unSponsorTo = baseConverter.transformTo(UserTo.class, helper.unUsuario());
        unosPropuestasTo.add(new PropuestaDePinoARootTo(unPino, unSponsorTo));
        unosPropuestasTo.add(new PropuestaDePinoARootTo(otroPino, unSponsorTo));
        TemaParaProponerPinosARootTo temaTo =
                TemaParaProponerPinosARootTo.create(unAutor, unaDuracion, unaObligatoriedad, unTitulo, unosPropuestasTo);

        TemaParaProponerPinosARoot tema = (TemaParaProponerPinosARoot)
                baseConverter.transformTo(TemaDeReunion.class, temaTo);

        assertThat(tema.getTitulo()).isEqualTo(temaTo.getTitulo());
        assertThat(tema.getDescripcion()).isEqualTo(temaTo.getDescripcion());
        assertThat(convertEnumToString(tema.getDuracion())).isEqualTo(temaTo.getDuracion());
        assertThat(convertEnumToString(tema.getObligatoriedad())).isEqualTo(temaTo.getObligatoriedad());
        assertThat(tema.propuestas().stream().map(PropuestaDePinoARoot::pino)).containsExactly(unPino, otroPino);
    }

    private String convertEnumToString(Object enumValue) {
        return baseConverter.transformTo(String.class, enumValue);
    }

}

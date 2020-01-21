package ar.com.kfgodel.temas.tos;

import ar.com.kfgodel.temas.application.Application;
import ar.com.kfgodel.temas.exceptions.TypeTransformerException;
import ar.com.kfgodel.temas.helpers.TestConfig;
import ar.com.kfgodel.temas.helpers.TestHelper;
import ar.com.kfgodel.temas.persistence.TestApplication;
import ar.com.kfgodel.transformbyconvention.api.TypeTransformer;
import convention.persistent.*;
import convention.rest.api.tos.TemaParaRepasarActionItemsTo;
import convention.rest.api.tos.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
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

    @After
    public void tearDown() {
        application.stop();
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
        String unLink = helper.unLink();

        TemaDeReunionConDescripcionTo temaTo =
                TemaDeReunionConDescripcionTo.create(unAutor, unaDuracion, unaObligatoriedad, unTitulo, unaDescripcion, unLink);

        TemaDeReunionConDescripcion tema = (TemaDeReunionConDescripcion)
                baseConverter.transformTo(TemaDeReunion.class, temaTo);

        assertThat(tema.getTitulo()).isEqualTo(temaTo.getTitulo());
        assertThat(tema.getDescripcion()).isEqualTo(temaTo.getDescripcion());
        assertThat(convertEnumToString(tema.getDuracion())).isEqualTo(temaTo.getDuracion());
        assertThat(convertEnumToString(tema.getObligatoriedad())).isEqualTo(temaTo.getObligatoriedad());
    }

    @Test
    public void testSePuedeConvertirDeUnTemaParaProponerPinosComoRootTo() {
        List<PropuestaDePinoARootTo> propuestas = unasPropuestasDePino(helper.unPino(), helper.otroPino());
        TemaParaProponerPinosARootTo temaTo = unTemaParaProponerPinosARootTo(propuestas);

        TemaParaProponerPinosARoot tema = (TemaParaProponerPinosARoot)
                baseConverter.transformTo(TemaDeReunion.class, temaTo);

        assertThat(tema.getTitulo()).isEqualTo(temaTo.getTitulo());
        assertThat(tema.getDescripcion()).isEqualTo(temaTo.getDescripcion());
        assertThat(convertEnumToString(tema.getDuracion())).isEqualTo(temaTo.getDuracion());
        assertThat(convertEnumToString(tema.getObligatoriedad())).isEqualTo(temaTo.getObligatoriedad());
        assertThat(tema.propuestas().stream().map(PropuestaDePinoARoot::pino))
                .containsExactly(helper.unPino(), helper.otroPino());
    }

    @Test
    public void testSePuedeConvertirDeUnTemaParaRepasarActionItemsTo() {
        UserTo unUserTo = helper.unUserTo();
        ActionItemTo unActionItemTo = helper.unActionItemTo(unUserTo);
        TemaDeMinutaTo temaDeMinutaTo = unTemaDeMinutaConActionItemsTo(unActionItemTo);
        TemaParaRepasarActionItemsTo temaTo = unTemaParaRepasarActionItemsTo(temaDeMinutaTo);

        TemaParaRepasarActionItems tema = (TemaParaRepasarActionItems)
                baseConverter.transformTo(TemaDeReunion.class, temaTo);

        assertThat(tema.getTitulo()).isEqualTo(temaTo.getTitulo());
        assertThat(tema.getDescripcion()).isEqualTo(temaTo.getDescripcion());
        assertThat(convertEnumToString(tema.getDuracion())).isEqualTo(temaTo.getDuracion());
        assertThat(convertEnumToString(tema.getObligatoriedad())).isEqualTo(temaTo.getObligatoriedad());
        assertThat(tema.getTemasParaRepasar().get(0).getActionItems().get(0).getDescripcion())
                .isEqualTo(unActionItemTo.getDescripcion());
        assertThat(tema.getTemasParaRepasar().get(0).getActionItems().get(0).getResponsables().get(0).getName())
                .isEqualTo(unUserTo.getName());
    }

    private TemaDeMinutaTo unTemaDeMinutaConActionItemsTo(ActionItemTo actionItem) {
        TemaDeMinutaTo temadeMinutaTo = new TemaDeMinutaTo();
        temadeMinutaTo.setFueTratado(true);
        temadeMinutaTo.setActionItems(Arrays.asList(actionItem));
        return temadeMinutaTo;
    }

    private TemaParaRepasarActionItemsTo unTemaParaRepasarActionItemsTo(TemaDeMinutaTo temadeMinutaTo) {
        Usuario unAutor = helper.unUsuario();
        String unaDuracion = convertEnumToString(TemaParaProponerPinosARoot.DURACION);
        String unaObligatoriedad = convertEnumToString(TemaParaProponerPinosARoot.OBLIGATORIEDAD);
        String unTitulo = TemaParaRepasarActionItems.TITULO;
        return TemaParaRepasarActionItemsTo.create(unAutor, unaDuracion, unaObligatoriedad, unTitulo, Arrays.asList(temadeMinutaTo));
    }

    private List<PropuestaDePinoARootTo> unasPropuestasDePino(String unPino, String otroPino){
        List<PropuestaDePinoARootTo> unosPropuestasTo = new ArrayList<>();
        UserTo unSponsorTo = baseConverter.transformTo(UserTo.class, helper.unUsuario());
        unosPropuestasTo.add(new PropuestaDePinoARootTo(unPino, unSponsorTo));
        unosPropuestasTo.add(new PropuestaDePinoARootTo(otroPino, unSponsorTo));
        return unosPropuestasTo;
    }

    private TemaParaProponerPinosARootTo unTemaParaProponerPinosARootTo(List<PropuestaDePinoARootTo> unosPropuestasTo) {
        Usuario unAutor = helper.unUsuario();
        String unaDuracion = convertEnumToString(TemaParaProponerPinosARoot.DURACION);
        String unaObligatoriedad = convertEnumToString(TemaParaProponerPinosARoot.OBLIGATORIEDAD);
        String unTitulo = TemaParaProponerPinosARoot.TITULO;
        return TemaParaProponerPinosARootTo.create(unAutor, unaDuracion, unaObligatoriedad, unTitulo, unosPropuestasTo);
    }

    @Test
    public void testSePuedeConvertirAUnTemaParaRepasarActionItemsTo() {
        TemaParaRepasarActionItems tema = helper.unTemaParaRepasarActionItems();

        TemaParaRepasarActionItemsTo temaTo = (TemaParaRepasarActionItemsTo)
                baseConverter.transformTo(TemaDeReunionTo.class, tema);

        assertThat(temaTo.getIdDeAutor()).isEqualTo(tema.getAutor().getId());
        assertThat(temaTo.getTitulo()).isEqualTo(tema.getTitulo());
        assertThat(temaTo.getDuracion()).isEqualTo(convertEnumToString(tema.getDuracion()));
        assertThat(temaTo.getObligatoriedad()).isEqualTo(convertEnumToString(tema.getObligatoriedad()));
        assertThat(temaTo.getTemasParaRepasar().get(0).getActionItems().get(0).getDescripcion())
                .isEqualTo("Una cosa para hacer");
        assertThat(temaTo.getTemasParaRepasar().get(0).getActionItems().get(0).getResponsables().get(0).getName())
                .isEqualTo("jorge");
    }

    private String convertEnumToString(Object enumValue) {
        return baseConverter.transformTo(String.class, enumValue);
    }

}

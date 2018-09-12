package ar.com.kfgodel.temas.notifications;

import convention.persistent.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;

public class OnDeleteResponsablesObserverTest {

    private RealMailSender mailSender;
    private OnDeleteResponsablesObserver onDeleteObserver;
    private List<ActionItem> oldActionItems;
    private List<ActionItem> newActionItems;
    private Usuario unUsuario;
    private Usuario otroUsuario;
    private TemaDeMinuta temaDeMinuta;
    private TemaDeReunion tema;
    private Reunion reunion;
    private ActionItem unActionItem;
    private ActionItem otroActionItem;
    private String subjectMail;
    private String descripcionMail;

    @Before
    public void setUp(){
        temaDeMinuta = new TemaDeMinuta();
        tema = new TemaDeReunion();
        tema.setTitulo("titulo");
        temaDeMinuta.setTema(tema);
        reunion = new Reunion();
        reunion.setId(1L);
        tema.setReunion(reunion);
        Minuta minuta = new Minuta();
        minuta.setReunion(reunion);
        temaDeMinuta.setMinuta(minuta);

        unUsuario = Usuario.create("juan","juan","juan","1","hola@10pines.com");
        otroUsuario = Usuario.create("pedro","pedro","pedro","2", "chau@10pines.com");


        unActionItem = new ActionItem();
        unActionItem.setId(1L);
        unActionItem.setDescripcion("una descripcion");
        unActionItem.setResponsables(Arrays.asList(unUsuario, otroUsuario));
        unActionItem.setTema(temaDeMinuta);

        otroActionItem = new ActionItem();
        otroActionItem.setDescripcion("una descripcion");
        otroActionItem.setResponsables(Arrays.asList(unUsuario));
        otroActionItem.setId(1L);
        otroActionItem.setTema(temaDeMinuta);

        oldActionItems = Arrays.asList(unActionItem);
        newActionItems = Arrays.asList(otroActionItem);

        mailSender = Mockito.mock(RealMailSender.class);
        onDeleteObserver = new OnDeleteResponsablesObserver(mailSender);
        subjectMail = "Fuiste removido del action item: titulo";
        descripcionMail = "Fuiste eliminado del action item: una descripcion. Para más información entrá en: http://votacion-roots.herokuapp.com/minuta/1/ver";
    }

    @Test
    public void siLosResponsablesSonLosMismosNoEnviaMail() {
        onDeleteObserver.notificar(oldActionItems, oldActionItems);
        Mockito.verify(mailSender, times(0)).sendMail(unUsuario.getMail(), subjectMail, descripcionMail);
        Mockito.verify(mailSender, times(0)).sendMail(otroUsuario.getMail(), subjectMail, descripcionMail);
    }

    @Test
    public void cuandoEliminoResponsablesDeUnActionItemEnviaMails() {
        onDeleteObserver.notificar(oldActionItems, newActionItems);
        Mockito.verify(mailSender, times(1)).sendMail(otroUsuario.getMail(), subjectMail, descripcionMail);
        Mockito.verify(mailSender, times(0)).sendMail(unUsuario.getMail(), subjectMail, descripcionMail);
    }
}

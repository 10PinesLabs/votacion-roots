package ar.com.kfgodel.temas.notifications;

import convention.persistent.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;

public class DeleteResponsablesObserverTest {

    private RealMailSender mailSender;
    private DeleteResponsablesObserver onDeleteObserver;
    private List<ActionItem> oldActionItems;
    private List<ActionItem> newActionItems;
    private Usuario unUsuario;
    private Usuario otroUsuario;

    @Before
    public void setUp(){
        unUsuario = Usuario.create("juan","juan","juan","1","hola@10pines.com");
        otroUsuario = Usuario.create("pedro","pedro","pedro","2", "chau@10pines.com");


        ActionItem unActionItem = new ActionItem();
        unActionItem.setId(1L);
        unActionItem.setDescripcion("una descripcion");
        unActionItem.setResponsables(Arrays.asList(unUsuario, otroUsuario));

        ActionItem otroActionItem = new ActionItem();
        otroActionItem.setDescripcion("una descripcion");
        otroActionItem.setResponsables(Arrays.asList(unUsuario));
        otroActionItem.setId(1L);

        oldActionItems = Arrays.asList(unActionItem);
        newActionItems = Arrays.asList(otroActionItem);

        mailSender = Mockito.mock(RealMailSender.class);
        onDeleteObserver = new DeleteResponsablesObserver(mailSender);
    }

    @Test
    public void siLosResponsablesSonLosMismosNoEnviaMail(){
        onDeleteObserver.notificar(oldActionItems, oldActionItems);
        Mockito.verify(mailSender,times(0)).sendMail(unUsuario.getMail(),"Fuiste removido de un action item","Fuiste eliminado de un action item. Para más información ingresá en: ");
        Mockito.verify(mailSender,times(0)).sendMail(otroUsuario.getMail(),"Fuiste removido de un action item","Fuiste eliminado de un action item. Para más información ingresá en: ");
    }

    @Test
    public void cuandoEliminoResponsablesDeUnActionItemEnviaMails(){
        onDeleteObserver.notificar(oldActionItems, newActionItems);
        Mockito.verify(mailSender,times(1)).sendMail(otroUsuario.getMail(),"Fuiste removido de un action item","Fuiste eliminado de un action item. Para más información ingresá en: ");
        Mockito.verify(mailSender,times(0)).sendMail(unUsuario.getMail(),"Fuiste removido de un action item","Fuiste eliminado de un action item. Para más información ingresá en: ");
    }
}

package ar.com.kfgodel.temas.domain;

import convention.persistent.ActionItem;
import convention.persistent.Usuario;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ActionItemTest {

    private List<Usuario> responsables;
    private ActionItem unActionItem;
    private ActionItem otroActionItem;

    @Before
    public void setUp(){
        responsables = Arrays.asList(Usuario.create("juan", "juan","juan", "juan",""));
        unActionItem = new ActionItem();
        unActionItem.setId(1L);
        unActionItem.setDescripcion("una descripcion");
        unActionItem.setResponsables(responsables);

        otroActionItem = new ActionItem();
        otroActionItem.setId(1L);
        otroActionItem.setDescripcion("una descripcion");
        otroActionItem.setResponsables(responsables);
    }

    @Test
    public void unActionItemEsIgualAOtroSiSusAtributosSonIguales(){
        assertThat(unActionItem.equals(otroActionItem)).isTrue();
    }

    @Test
    public void unActionItemEsDistintoAOtroSiTienenDistintaDescripcion(){
        otroActionItem.setDescripcion("otra descripcion");
        assertThat(unActionItem.equals(otroActionItem)).isFalse();
    }

    @Test
    public void unActionItemEsDistintoAOtroSiTienenDistintosResponsables(){
        otroActionItem.setResponsables(Arrays.asList(Usuario.create("pedro","pedro","pedro","pedro","")));
        assertThat(unActionItem.equals(otroActionItem)).isFalse();
    }

    @Test
    public void unActionItemEsDistintoAOtroSiTienenIdDistintos(){
        otroActionItem.setId(2L);
        assertThat(unActionItem.equals(otroActionItem)).isFalse();
    }

}

package ar.com.kfgodel.temas.apiRest;

import convention.persistent.Reunion;
import convention.persistent.TemaDeReunion;
import convention.persistent.Usuario;
import convention.rest.api.UserResource;
import convention.rest.api.tos.UserTo;
import org.junit.Assert;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by fede on 05/07/17.
 */
public class UserResourceTest extends ResourcesTemasTest {

    UserResource userResource;
    Reunion unaReunion;
    TemaDeReunion unTema;

    @Override
    public void setUp() {
        super.setUp();
        userResource =  UserResource.create(app.injector());
        unaReunion = reunionService.save(new Reunion());
        unTema = TemaDeReunion.create();
        unTema.agregarInteresado(user);
        unTema.setReunion(unaReunion);
        unaReunion.setTemasPropuestos(Arrays.asList(unTema));

    }

    @Test
    public void AlPedirLosNoVotantesDeUnaReunionMeDevuelveTodosLosUsuariosQueNoVotaron(){
        unaReunion = reunionService.update(unaReunion);

        Assert.assertTrue(userResource.getUsersQueNoVotaron(unaReunion.getId()).stream().allMatch(userTo -> userTo.getId() != userId));
        Assert.assertTrue(userResource.getUsersQueNoVotaron(unaReunion.getId()).stream().anyMatch(userTo -> userTo.getId().equals(otherUserId)));
    }

    @Test
    public void AlPedirLosVotantesDeUnaReunionMeDevuelveTodosLosUsuariosQueVotaron(){
        unaReunion = reunionService.update(unaReunion);
        Long idReunion = unaReunion.getId();
        List<UserTo> usersQueVotaron = userResource.getUsersQueVotaron(idReunion);
        List<UserTo> usersQueVotaronYNoVotaron = userResource.getUsersQueNoVotaron(idReunion);
        usersQueVotaronYNoVotaron.addAll(usersQueVotaron);
        List<UserTo> todosLosUsuarios = userResource.getAllUsers();

        assertThat(usersQueVotaron).extracting("id").contains(userId);
        assertThat(usersQueVotaronYNoVotaron).hasSameSizeAs(todosLosUsuarios);
    }
}

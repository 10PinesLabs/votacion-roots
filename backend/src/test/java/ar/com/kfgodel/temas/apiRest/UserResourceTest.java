package ar.com.kfgodel.temas.apiRest;

import convention.persistent.Reunion;
import convention.persistent.TemaDeReunion;
import convention.rest.api.UserResource;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

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

        Assert.assertTrue(userResource.getUsersQueVotaron(unaReunion.getId()).stream().anyMatch(userTo -> userTo.getId().equals(userId)));
        Assert.assertFalse(userResource.getUsersQueVotaron(unaReunion.getId()).stream().anyMatch(userTo -> userTo.getId().equals(otherUserId)));
        Assert.assertTrue(userResource.getUsersQueVotaron(unaReunion.getId()).size()>0);
    }
}

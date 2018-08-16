package convention.rest.api;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import convention.persistent.Usuario;
import convention.rest.api.tos.UserTo;
import convention.services.ReunionService;
import convention.services.UsuarioService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This type is the resource API for users
 * Created by kfgodel on 03/03/15.
 */
@Produces("application/json")
@Consumes("application/json")
public class UserResource{


    @Inject
    UsuarioService userService;

    @Inject
    ReunionService reunionService;

    private ResourceHelper resourceHelper;

    private static final Type LIST_OF_USER_TOS = new ReferenceOf<List<UserTo>>() {
    }.getReferencedType();

    @GET
    @Path("current")
    public UserTo getCurrent(@Context SecurityContext securityContext) {
        Long currentUserId = getResourceHelper().idDeUsuarioActual(securityContext);
        return getResourceHelper().convertir(userService.get(currentUserId), UserTo.class);
    }

    @GET
    @Path("noVotaron/{reunionId}")
    public List<UserTo> getUsersQueNoVotaron(@PathParam("reunionId") Long reunionId)
    {

                List<Usuario> usuarios=userService.getAll();
        List<Usuario> votantes=reunionService.get(reunionId).usuariosQueVotaron();
        usuarios=usuarios.stream().filter(usuario ->
                !votantes.stream().anyMatch(votante -> votante.getId().equals(usuario.getId()) )).collect(Collectors.toList());
        return getResourceHelper().convertir(usuarios, LIST_OF_USER_TOS);
    }


    @GET
    public List<UserTo> getAllUsers() {

        return getResourceHelper().convertir(userService.getAll(), LIST_OF_USER_TOS);
    }

    @GET
    @Path("/{userId}")
    public UserTo getSingleUser(@PathParam("userId") Long userId) {
        return getResourceHelper().convertir(userService.get(userId), UserTo.class);
    }


    @PUT
    @Path("/{userId}")
    public UserTo updateUser(UserTo newUserState, @PathParam("userId") Long userId) {
        Usuario usuarioUpdateado = userService.update(getResourceHelper().convertir(newUserState, Usuario.class));
        return getResourceHelper().convertir(usuarioUpdateado, UserTo.class);
    }


    @DELETE
    @Path("/{userId}")
    public void deleteUser(@PathParam("userId") Long userId) {
        userService.delete(userId);
    }

    public static UserResource create(DependencyInjector appInjector) {
        UserResource userResource = new UserResource();
        userResource.resourceHelper=ResourceHelper.create(appInjector);
        userResource.userService = appInjector.createInjected(UsuarioService.class);
        userResource.reunionService = appInjector.createInjected(ReunionService.class);
        userResource.getResourceHelper().bindAppInjectorTo(UserResource.class,userResource);
        return userResource;
    }

    public ResourceHelper getResourceHelper() {
        return resourceHelper;
    }

    @GET
    @Path("votaron/{reunionId}")
    public List<UserTo> getUsersQueVotaron(@PathParam("reunionId")Long reunionId) {
        List<Usuario> votantes = reunionService.get(reunionId).usuariosQueVotaron().stream().distinct().collect(Collectors.toList());
        return getResourceHelper().convertir(votantes, LIST_OF_USER_TOS);
    }
}

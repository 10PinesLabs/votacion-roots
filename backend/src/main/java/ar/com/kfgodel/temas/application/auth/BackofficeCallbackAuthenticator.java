package ar.com.kfgodel.temas.application.auth;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import ar.com.kfgodel.temas.filters.users.UserByBackofficeId;
import ar.com.kfgodel.webbyconvention.api.auth.WebCredential;
import convention.persistent.Usuario;
import convention.rest.api.tos.BackofficeUserTo;
import org.h2.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.function.Function;

/**
 * This type knows how to authenticate users from the backoffice callback
 * Created by kfgodel on 14/01/17.
 */
public abstract class BackofficeCallbackAuthenticator implements Function<WebCredential, Optional<Object>> {
  public static Logger LOG = LoggerFactory.getLogger(BackofficeCallbackAuthenticator.class);

  protected DependencyInjector appInjector;

  @Override
  public abstract Optional<Object> apply(WebCredential webCredential);

  protected Optional<BackofficeUserTo> createToFrom(WebCredential webCredential) {
    String denied = webCredential.getRequestParameter("denied");
    if (!StringUtils.isNullOrEmpty(denied)) {
      // Nos denegaron el acceso
      return Optional.empty();
    }
    String uid = webCredential.getRequestParameter("uid");
    String email = webCredential.getRequestParameter("email");
    String username = webCredential.getRequestParameter("username");
    String fullname = webCredential.getRequestParameter("full_name");
    String root = webCredential.getRequestParameter("root");
    String hmac = webCredential.getRequestParameter("hmac");
    if (!isAValidHmac(uid, email, username, fullname, root, hmac)) {
      // Por alguna razon no coincide el hmac, lo rechazamos
      return Optional.empty();
    }
    BackofficeUserTo userTo = BackofficeUserTo.create(uid, email, username, fullname, root, denied, hmac);
    return Optional.of(userTo);
  }

  private boolean isAValidHmac(String uid, String email, String username, String fullName, String root, String hmacProvisto) {
    String key = "CHANGE_ME";
    String data = "uid=" + uid + "&email=" + email + "&username=" + username + "&full_name=" + fullName + "&root=" + root;

    String hmacCalculado;
    try {
      hmacCalculado = calcularHmacPara(data, key);
    } catch (Exception e) {
      LOG.error("Se produjo un error al calcular el hmac de " + data, e);
      return false;
    }
    boolean coinciden = hmacCalculado.equals(hmacProvisto);
    if (!coinciden) {
      LOG.warn("No coincide el hmac calculado[{}] != provisto[{}] para la data[{}]", hmacCalculado, hmacCalculado, data);
    }
    return coinciden;
  }

  private String calcularHmacPara(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
    String algorithm = "HmacSHA256";
    Mac mac = Mac.getInstance(algorithm);
    mac.init(new SecretKeySpec(key.getBytes(), algorithm));
    byte[] generatedHmacBytes = mac.doFinal(data.getBytes("UTF-8"));
    StringBuilder sb = new StringBuilder();
    for (byte b : generatedHmacBytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }

  protected Long buscarIdDelUsuarioEnLaBase(BackofficeUserTo backofficeUser) {
    Usuario usuario = createOperation()
      .insideASession()
      .applying(UserByBackofficeId.create(backofficeUser.uid))
      .mapping(usuarioExistente -> usuarioExistente.orElseGet(() -> crearUsuarioNuevo(backofficeUser)))
      .mapping(user -> verificarSiTieneMail(user, backofficeUser))
            .get();
    return usuario.getId();
  }

    private Usuario verificarSiTieneMail(Usuario user, BackofficeUserTo backofficeUser) {
      if(!user.getMail().equals(backofficeUser.email)) {
          return createOperation()
                  .insideATransaction()
                  .taking(user)
                  .mapping((usuario -> actualizarEmail(usuario, backofficeUser.email)))
                  .applyingResultOf(Save::create)
                  .get();
      }
      return user;
    }

    private Usuario actualizarEmail(Usuario usuario, String email) {
        usuario.setMail(email);
        return usuario;
    }

    private Usuario crearUsuarioNuevo(BackofficeUserTo backofficeUser) {
    return createOperation()
      .insideATransaction()
      .taking(Usuario.create(backofficeUser.fullName,
        backofficeUser.username,
        "password",
        backofficeUser.uid,
        backofficeUser.email))
      .applyingResultOf(Save::create)
      .get();
  }

  private ApplicationOperation createOperation() {
    return ApplicationOperation.createFor(appInjector);
  }

}

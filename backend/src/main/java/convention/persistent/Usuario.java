package convention.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * This type represents a user in the database Created by kfgodel on 22/03/15.
 */
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {Usuario.login_FIELD, Usuario.password_FIELD}),
    @UniqueConstraint(columnNames = Usuario.backofficeId_FIELD)
})
public class Usuario extends PersistableSupport {

  public static final String name_FIELD = "name";
  public static final String login_FIELD = "login";
  public static final String password_FIELD = "password";
  public static final String backofficeId_FIELD = "backofficeId";
  public static final String mail_FIELD = "mail";

  private String name;
  private String login;
  private String password;
  private String backofficeId;
  private String mail;

  public static Usuario create(String name, String login, String password, String backofficeId, String mail) {
    Usuario usuario = new Usuario();
    usuario.setName(name);
    usuario.setLogin(login);
    usuario.setPassword(password);
    usuario.setBackofficeId(backofficeId);
    usuario.setMail(mail);
    return usuario;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getBackofficeId() {
    return backofficeId;
  }

  public void setBackofficeId(String backofficeId) {
    this.backofficeId = backofficeId;
  }

  public String getMail() {
    return mail;
  }
}

package convention.rest.api.tos;

import ar.com.kfgodel.appbyconvention.tos.PersistableToSupport;
import convention.persistent.Usuario;
import net.sf.kfgodel.bean2bean.annotations.CopyFrom;
import net.sf.kfgodel.bean2bean.annotations.CopyFromAndTo;

/**
 * This type represents a user for the frontend
 * Created by kfgodel on 03/03/15.
 */
public class UserTo extends PersistableToSupport {

  @CopyFromAndTo(Usuario.name_FIELD)
  private String name;

  @CopyFromAndTo(Usuario.login_FIELD)
  private String login;

  @CopyFromAndTo(Usuario.backofficeId_FIELD)
  private String backofficeId;

  @CopyFrom(Usuario.momentoDeCreacion_FIELD)
  private String creation;

  @CopyFrom(Usuario.momentoDeUltimaModificacion_FIELD)
  private String modification;

  @CopyFromAndTo(Usuario.mail_FIELD)
  private String mail;

  public String getBackofficeId() {
    return backofficeId;
  }

  public void setBackofficeId(String backofficeId) {
    this.backofficeId = backofficeId;
  }

  public String getMail() {
    return mail;
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

  public String getCreation() {
    return creation;
  }

  public void setCreation(String creation) {
    this.creation = creation;
  }

  public String getModification() {
    return modification;
  }

  public void setModification(String modification) {
    this.modification = modification;
  }

}

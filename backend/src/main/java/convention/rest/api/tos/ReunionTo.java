package convention.rest.api.tos;

import ar.com.kfgodel.appbyconvention.tos.PersistableToSupport;
import convention.persistent.Reunion;
import net.sf.kfgodel.bean2bean.annotations.CopyFrom;
import net.sf.kfgodel.bean2bean.annotations.CopyFromAndTo;

import java.util.List;

/**
 * Esta clase representa el TO de una reunion para ser editada
 * Created by kfgodel on 08/10/16.
 */
public class ReunionTo extends PersistableToSupport {

  @CopyFromAndTo(Reunion.fecha_FIELD)
  private String fecha;

  @CopyFrom(Reunion.status_FIELD)
  private String status;

  @CopyFromAndTo(Reunion.temasPropuestos_FIELD)
  private List<TemaDeReunionTo> temasPropuestos;

  public String getFecha() {
    return fecha;
  }

  public void setFecha(String fecha) {
    this.fecha = fecha;
  }

  public List<TemaDeReunionTo> getTemasPropuestos() {
    return temasPropuestos;
  }

  public void setTemasPropuestos(List<TemaDeReunionTo> temasPropuestos) {
    this.temasPropuestos = temasPropuestos;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}

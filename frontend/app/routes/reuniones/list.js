import Ember from "ember";
import AuthenticatedRoute from "ateam-ember-authenticator/mixins/authenticated-route";
import NavigatorInjected from "../../mixins/navigator-injected";
import ReunionServiceInjected from "../../mixins/reunion-service-injected";
import UserServiceInjected from "../../mixins/user-service-injected";
import Tema from "../../concepts/tema";

export default Ember.Route.extend(AuthenticatedRoute, UserServiceInjected, ReunionServiceInjected, NavigatorInjected, {
  model() {
    return this.promiseWaitingFor(this.reunionService().getAllReuniones())
      .whenInterruptedAndReauthenticated(() => {
        this.navigator().navigateToReuniones();
      })
      .then((reuniones) => {
        reuniones.forEach((reunion) => {
          this._usarInstanciasDeTemas(reunion, null);
          this._setearFechaFormateada(reunion);
          return reunion;
        });
        return reuniones;
      });
  },

  _setearFechaFormateada(reunion){
    const fechaDeReunion =  this._formatearFecha(reunion.get('fecha'));
    return reunion.set('fechaFormateada',fechaDeReunion);
  },

  _formatearFecha(fechaEnString) {
    const fecha = moment(fechaEnString);
    return [fecha.date(), nombreDeMeses.nombreParaElMes(fecha.month()), fecha.year()].join('-');
  },


  _usarInstanciasDeTemas(reunion, usuarioActual) {
    let temasPropuestos = reunion.get('temasPropuestos');
    temasPropuestos.forEach((temaPropuesto, index) => {
      let objetoEmber = temasPropuestos[index];
      objetoEmber.set('usuarioActual', usuarioActual);
      let tema = Tema.create(objetoEmber);
      temasPropuestos[index] = tema;
    });
  }

})
;

const nombreDeMeses = Object.freeze({
  0: 'Ene',
  1: 'Feb',
  2: 'Mar',
  3: 'Abr',
  4: 'May',
  5: 'Jun',
  6: 'Jul',
  7: 'Ago',
  8: 'Sep',
  9: 'Oct',
  10: 'Nov',
  11: 'Dic',
  nombreParaElMes(unMes) {
    return this[unMes] || 0;
  }
});

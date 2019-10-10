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
    const fechaParseada = fechaEnString.split('-');

    //new Date(unaFecha) devuelve el dia anterior a unaFecha, no asi new Date(anio, mes, dia)
    const fecha = new Date(fechaParseada[0], fechaParseada[1], fechaParseada[2]);
    return [fecha.getDate(), nombreDeMeses.nombreParaElMes(fecha.getMonth()), fecha.getFullYear()].join('-');
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
  1: 'Ene',
  2: 'Feb',
  3: 'Mar',
  4: 'Abr',
  5: 'May',
  6: 'Jun',
  7: 'Jul',
  8: 'Ago',
  9: 'Sep',
  10: 'Oct',
  11: 'Nov',
  12: 'Dic',
  nombreParaElMes(unMes) {
    return this[unMes] || 1;
  }
});

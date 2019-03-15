import Ember from 'ember';
import MinutaServiceInjected from "../mixins/minuta-service-injected";
import TemaDeMinutaServiceInjected from "../mixins/tema-de-minuta-service-injected";
import NavigatorInjected from "../mixins/navigator-injected";
import UserServiceInjected from "../mixins/user-service-injected";

export default Ember.Component.extend(MinutaServiceInjected, TemaDeMinutaServiceInjected, NavigatorInjected, UserServiceInjected, {
  classNames: ['card'],

  mostrarDetalle: false,
  textoExtendido: false,

  usuarios: Ember.computed('model.usuarios', function () {
    return this.get('model.usuarios');
  }),

  actions: {
    verDetalleDeTema() {
      this._mostrarDetalle();
    },

    ocultarDetalleDeTema() {
      return this._ocultarDetalle();
    }

  },

  _mostrarDetalle() {
    this.set('mostrandoDetalle', true);
  },

  _ocultarDetalle() {
    this.set('mostrandoDetalle', false);
    this.set('textoExtendido', false);
  },


});

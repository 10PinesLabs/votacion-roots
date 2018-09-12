import Ember from 'ember';
import MinutaServiceInjected from "../mixins/minuta-service-injected";
import TemaDeMinutaServiceInjected from "../mixins/tema-de-minuta-service-injected";
import NavigatorInjected from "../mixins/navigator-injected";
import UserServiceInjected from "../mixins/user-service-injected";

export default Ember.Component.extend(MinutaServiceInjected, TemaDeMinutaServiceInjected, NavigatorInjected, UserServiceInjected, {
  classNames: ['card'],

  mostrarDetalle: false,

  usuarios: Ember.computed('model.usuarios', function () {
    return this.get('model.usuarios');
  }),

  actions: {
    verDetalleDeTema(tema) {
      this._mostrarDetalle(tema);
    },

    ocultarDetalleDeTema(tema) {
      return this._ocultarDetalle(tema);
    },

  },
  _mostrarDetalle(tema) {
    var indiceClickeado = this.get('model.minuta.temas').indexOf(tema);
    this.set('indiceSeleccionado', indiceClickeado);
    this.set('mostrandoDetalle', true);
  },

  _ocultarDetalle(tema) {
    var indiceClickeado = this.get('model.minuta.temas').indexOf(tema);
    this.set('indiceSeleccionado', indiceClickeado);
    this.set('mostrandoDetalle', false);
  },

  _recargarLista() {
    this.get('router').refresh();
  },

});

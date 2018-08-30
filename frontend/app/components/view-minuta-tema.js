import Ember from 'ember';
import MinutaServiceInjected from "../mixins/minuta-service-injected";
import TemaDeMinutaServiceInjected from "../mixins/tema-de-minuta-service-injected";
import NavigatorInjected from "../mixins/navigator-injected";
import UserServiceInjected from "../mixins/user-service-injected";

export default Ember.Component.extend(MinutaServiceInjected, TemaDeMinutaServiceInjected, NavigatorInjected, UserServiceInjected, {

  mostrarDetalle: Ember.computed('mostrarDetalle', function () {
    return false;
  }),

  actions: {
    verDetalleDeTema(tema){
      return this._mostrarDetalle(tema);
    },

    ocultarDetalleDeTema(tema){
      return this._ocultarDetalle(tema);
    }
  },

  _mostrarDetalle(tema){
    var indiceClickeado = this.get('model.minuta.temas').indexOf(tema);
    this.set('indiceSeleccionado', indiceClickeado);
    this.set('mostrarDetalle', true);
  },

  _ocultarDetalle(tema){
    var indiceClickeado = this.get('model.minuta.temas').indexOf(tema);
    this.set('indiceSeleccionado', indiceClickeado);
    this.set('mostrarDetalle', false);
  }
});

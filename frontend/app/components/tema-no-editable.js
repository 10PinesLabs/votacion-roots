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
    expandirDescripcion(){
      this._extenderTexto();
    },
    colapsarDescripcion(){
      this._colapsarTexto();
    },
    verDetalleDeTema() {
      this._mostrarDetalle();
    },
    ocultarDetalleDeTema() {
      this._ocultarDetalle();
      this._colapsarTexto();
    }
  },

  _extenderTexto(){
    this.set('textoExtendido', true);
  },
  _colapsarTexto(){
    this.set('textoExtendido', false);
  },
  _mostrarDetalle() {
    this.set('mostrandoDetalle', true);
  },
  _ocultarDetalle() {
    this.set('mostrandoDetalle', false);
  },

});

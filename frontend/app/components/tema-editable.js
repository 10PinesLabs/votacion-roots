import Ember from 'ember';
import MinutaServiceInjected from "../mixins/minuta-service-injected";
import TemaDeMinutaServiceInjected from "../mixins/tema-de-minuta-service-injected";
import NavigatorInjected from "../mixins/navigator-injected";
import UserServiceInjected from "../mixins/user-service-injected";

export default Ember.Component.extend(MinutaServiceInjected, TemaDeMinutaServiceInjected, NavigatorInjected, UserServiceInjected, {
  classNames: ['card'],

  mostrarDetalle: false,
  textoExtendido: false,
  agregarItem: false,

  usuarios: Ember.computed('model.usuarios', function () {
    return this.get('model.usuarios');
  }),

  temaTratado: Ember.computed('model', function () {
    return this.get('temaDeMinuta').fueTratado;
  }),

  actions: {
    verDetalleDeTema() {
      this._mostrarDetalle();
    },

    ocultarDetalleDeTema() {
      return this._ocultarDetalle();
    },

    agregarActionItem() {
      this._agregarNuevoActionItem();
    },

    ocultarAgregadoActionItem() {
      this.set('agregarItem', false);
    },

    soloGuardar(actionItem) {
      this._guardar(actionItem).then(() => this._recargarLista());
    },

    guardarYCrearOtro(actionItem) {
      this._guardar(actionItem);
      this._agregarNuevoActionItem();
    },
  },

  _mostrarDetalle() {
    this.set('mostrandoDetalle', true);
  },

  _ocultarDetalle() {
    this.set('mostrandoDetalle', false);
    this.set('textoExtendido', false);
  },

  _recargarLista() {
    this.get('router').refresh();
  },

});

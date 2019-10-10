import Ember from "ember";
import ReunionServiceInjected from "../../mixins/reunion-service-injected";
import NavigatorInjected from "../../mixins/navigator-injected";
import DuracionesServiceInjected from "../../mixins/duraciones-service-injected";
import UserServiceInjected from "../../mixins/user-service-injected";
import MinutaServiceInjected from "../../mixins/minuta-service-injected";

export default Ember.Controller.extend(ReunionServiceInjected, MinutaServiceInjected, UserServiceInjected, NavigatorInjected, DuracionesServiceInjected, {
  init(){
    this._super(...arguments);
    this._traerDuraciones().then( () => this.set('indiceSeleccionado', 0));
  },

  reunionSeleccionada: Ember.computed('model.[]', 'indiceSeleccionado', function () {
    var indiceSeleccionado = this.get('indiceSeleccionado');
    var reuniones = this._reuniones();
    return reuniones.objectAt(indiceSeleccionado);
  }),

  reunionCerrada: Ember.computed('reunionSeleccionada', function () {
    return this.get('reunionSeleccionada.status') === "CERRADA" || this.get('reunionSeleccionada.status') === "CON_MINUTA";
  }),

  reunionMinuteada: Ember.computed('reunionSeleccionada', 'minuta', function () {
    return this.get('reunionSeleccionada.status') !== 'CON_MINUTA';
  }),

  actions: {

    verReunion(reunion) {
      this._traerDuraciones().then(() => {
        this._mostrarDetalleDe(reunion);
      });
    },

    editarReunion(reunion) {
      this.navigator().navigateToReunionesEdit(reunion.get('id'));
    },

    crearReunion() {
      this._guardarNuevaYRecargar();
    },

    recargarLista(){
      this._recargarLista();
    }

  },

  _mostrarDetalleDe(reunion){
    const indexReunionDefault = 0;
    const indiceClickeado = this._reuniones().indexOf(reunion) || indexReunionDefault;
    this.set('indiceSeleccionado', indiceClickeado);
  },

  _guardarNuevaYRecargar() {
    this.reunionService().createReunion(Ember.Object.create())
      .then(() => {
        this._recargarLista();
      });
  },

  _recargarLista() {
    this.get('target.router').refresh();
  },

  _reuniones() {
    return this.get('model');
  },

  _traerDuraciones() {
    return this.duracionesService().getAll().then((duraciones) => {
      this.set('duraciones', duraciones);
    });
  },

  _obtenerDuracionDeTema(unTema) {
    var duraciones = this.get('duraciones');
    return duraciones.find(function (duracion) {
      return duracion.nombre === unTema.duracion;
    });
  },

});

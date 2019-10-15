import Ember from "ember";
import ReunionServiceInjected from "../../mixins/reunion-service-injected";
import NavigatorInjected from "../../mixins/navigator-injected";
import DuracionesServiceInjected from "../../mixins/duraciones-service-injected";
import UserServiceInjected from "../../mixins/user-service-injected";
import MinutaServiceInjected from "../../mixins/minuta-service-injected";
import estadoDeReunion from "./estadoDeReunion";

export default Ember.Controller.extend(ReunionServiceInjected, MinutaServiceInjected, UserServiceInjected, NavigatorInjected, DuracionesServiceInjected, {
  
  guardarHabilitado: Ember.computed('fechaNuevaReunion', function() {
    return this.get('fechaNuevaReunion') && !this.get('guardando');
  }),

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
    return this.get('reunionSeleccionada.status') === estadoDeReunion.CERRADA || this.get('reunionSeleccionada.status') === estadoDeReunion.CON_MINUTA;
  }),

  reunionMinuteada: Ember.computed('reunionSeleccionada', 'minuta', function () {
    return this.get('reunionSeleccionada.status') !== estadoDeReunion.CON_MINUTA;
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

    recargarLista() {
      this._recargarLista();
    },

    guardarNuevaReunion() {
      this.set('guardando', true);
      this._guardarNuevaYRecargar(this.get('fechaNuevaReunion'));
      this.set('mostrarModal', false);
    },

    crearReunion() {
      this.set('mostrarModal', true);
    },

    cerrarEditor(){
      this.set('mostrarModal', false);
    }
  },

  _mostrarDetalleDe(reunion) {
    const indexReunionDefault = 0;
    const indiceClickeado = this._reuniones().indexOf(reunion) || indexReunionDefault;
    this.set('indiceSeleccionado', indiceClickeado);
  },

  _guardarNuevaYRecargar(fechaNuevaReunion) {
    this.reunionService().createReunion(Object.assign({}, Ember.Object.create(), {fecha: fechaNuevaReunion}))
      .then(() => {
        this.set('guardando', false);
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

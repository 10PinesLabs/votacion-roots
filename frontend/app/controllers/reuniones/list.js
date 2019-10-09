import Ember from "ember";
import ReunionServiceInjected from "../../mixins/reunion-service-injected";
import NavigatorInjected from "../../mixins/navigator-injected";
import DuracionesServiceInjected from "../../mixins/duraciones-service-injected";
import UserServiceInjected from "../../mixins/user-service-injected";
import MinutaServiceInjected from "../../mixins/minuta-service-injected";

export default Ember.Controller.extend(ReunionServiceInjected, MinutaServiceInjected, UserServiceInjected, NavigatorInjected, DuracionesServiceInjected, {

  reunionSeleccionada: Ember.computed('model.[]', 'indiceSeleccionado', function () {
    var indiceSeleccionado = this.get('indiceSeleccionado');
    var reuniones = this._reuniones();
    return reuniones.objectAt(indiceSeleccionado);
  }),

  reunionCerrada: Ember.computed('reunionSeleccionada', function () {
    let estaCerrada = (this.get('reunionSeleccionada.status') === "CERRADA") || (this.get('reunionSeleccionada.status') === "CON_MINUTA");
    this.set('duracionDeReunion', estaCerrada ? 180 : 0);
    return estaCerrada;
  }),

  reunionMinuteada: Ember.computed('reunionSeleccionada', 'minuta', function () {
    return this.get('reunionSeleccionada.status') !== 'CON_MINUTA';
  }),

  temasEstimados: Ember.computed('duracionDeReunion', function () {

    var temas = this.get('reunionSeleccionada.temasPropuestos');
    var duracionRestante = this.duracionDeReunion;
    var i = 0;
    var temasQueEntran = [];
    while (i < temas.length && duracionRestante >= this._obtenerDuracionDeTema(temas.get(i)).cantidadDeMinutos) {
      temasQueEntran.push(temas.get(i));
      duracionRestante = duracionRestante - this._obtenerDuracionDeTema(temas.get(i)).cantidadDeMinutos;
      i++;
    }
    return temasQueEntran;
  }),

  ultimoTemaQueEntra: Ember.computed('temasEstimados,reunionSeleccionada', function () {
    var temasEstimados = this.get('temasEstimados');
    return temasEstimados[temasEstimados.length - 1];
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

import Ember from 'ember';
import NavigatorInjected from "../mixins/navigator-injected";
import DuracionesServiceInjected from "../mixins/duraciones-service-injected";
import ReunionServiceInjected from "../mixins/reunion-service-injected";
import estadoDeReunion from "../controllers/reuniones/estadoDeReunion";
import {promiseHandling} from "../helpers/promise-handling";
import MinutaServiceInjected from "../mixins/minuta-service-injected";

export default Ember.Component.extend(NavigatorInjected, ReunionServiceInjected, DuracionesServiceInjected, MinutaServiceInjected, {

  reunionSeleccionada: Ember.computed('reunion', function () {
    let reunion = this.get('reunion');
    this._setearTemasTratadosYNoTratados(reunion);
    return reunion;
  }),

  duracionReunion: 180,

  tableTitles: Ember.computed('reunionCerrada', function () {
    const titulosDefault = '#, Título, Autor, Duración, Votos';
    const titulosConTemasTratados = titulosDefault + ', Tratado';
    return this.get('reunionCerrada') ? titulosConTemasTratados : titulosDefault;
  }),

  temasEstimados: Ember.computed('duracionDeReunion', function () {
    const temas = this.get('reunionSeleccionada.temasPropuestos');
    let duracionRestante = this.duracionReunion;
    let i = 0;
    let temasQueEntran = [];
    while (i < temas.length && duracionRestante >= this._obtenerDuracionDeTema(temas.get(i)).cantidadDeMinutos) {
      temasQueEntran.push(temas.get(i));
      duracionRestante = duracionRestante - this._obtenerDuracionDeTema(temas.get(i)).cantidadDeMinutos;
      i++;
    }
    return temasQueEntran;
  }),

  ultimoTemaQueEntra: Ember.computed('temasEstimados,reunionSeleccionada', function () {
    const temasEstimados = this.get('temasEstimados');
    return temasEstimados[temasEstimados.length - 1];
  }),

  actions: {
    masOpciones() {
      const mostrarOpciones = this.get('showOptions');
      this.set('showOptions', !mostrarOpciones);
    },

    eliminarReunion() {
      const reunion = this.get('reunionSeleccionada');
      this._cerrarMenu();
      this.reunionService().removeReunion(reunion)
        .then(() => this.recargarLista());
    },
    mostrarConfirmacionParaEliminarReunion(){
      this.set('modalDeEliminarReunionAbierto', true);
    },

    compartirReunion(reunion) {
      const baseUrl = window.location.host;
      const minutaUrl = baseUrl + "/minuta/" + reunion.id + "/ver";
      const reunionUrl = baseUrl + '/reuniones/' + reunion.id;

      const linkToShare = reunion.status === estadoDeReunion.PENDIENTE ? reunionUrl : minutaUrl ;

      promiseHandling(navigator.clipboard.writeText(linkToShare))
        .then(() => {
          this.set('showCopyToClipboardMessage', true);
          setTimeout(() => {
            this.set('showCopyToClipboardMessage', false);
            this._cerrarMenu();
          }, 1000);
        });
    },

    editarReunion(reunion) {
      this._cerrarMenu();
      const reunionId = reunion.get('id');
      estadoDeReunion.PENDIENTE === reunion.status ? this.navigator().navigateToReunionesEdit(reunionId) : this.navigator().navigateToVerMinuta(reunionId);
    },

  },

  _setearTemasTratadosYNoTratados(reunion) {
    if (!reunion || reunion.status === 'PENDIENTE') return;

    promiseHandling(this.minutaService().getMinutaDeReunion(reunion.id))
      .then(minuta => {
        const filtrarTemaPropuestoEnMinuta = temaPropuesto => minuta.temas.filter(tema => tema.tema.id === temaPropuesto.id)[0];
        reunion.temasPropuestos.forEach(temaPropuesto => temaPropuesto.set('fueTratado', filtrarTemaPropuestoEnMinuta(temaPropuesto).fueTratado));
      });
  },

  _obtenerDuracionDeTema(unTema) {
    var duraciones = this.get('duraciones');
    return duraciones.find(function (duracion) {
      return duracion.nombre === unTema.duracion;
    });
  },

  _cerrarMenu(){
    this.set('showOptions', false);
  }
});

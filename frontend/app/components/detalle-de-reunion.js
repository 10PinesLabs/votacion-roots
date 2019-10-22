import Ember from 'ember';
import NavigatorInjected from "../mixins/navigator-injected";
import DuracionesServiceInjected from "../mixins/duraciones-service-injected";
import ReunionServiceInjected from "../mixins/reunion-service-injected";
import estadoDeReunion from "../controllers/reuniones/estadoDeReunion";
import {promiseHandling} from "../helpers/promise-handling";

export default Ember.Component.extend(NavigatorInjected, ReunionServiceInjected, DuracionesServiceInjected, {
  duracionReunion: 180,


  tableTitles: Ember.computed('reunionCerrada', function () {
    const titulosDefault = '#, Título, Autor, Duración, Votos';
    const titulosConTemasTratados = titulosDefault + ', Tratado';
    return this.get('reunionCerrada') ? titulosConTemasTratados : titulosDefault;
  }),

  temasEstimados: Ember.computed('reunionSeleccionada', function () {
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

    eliminarReunion(reunion) {
      this._cerrarMenu();
      this.reunionService().removeReunion(reunion)
        .then(() => this.recargarLista());
    },

    reabrirReunionSeleccionada(){
      this.set('modalDeReabrirReunionAbierto', false);
      const reunion = this.get('reunionSeleccionada');
      this._reabrirVotacion(reunion)
        .then(() => this.navigator().navigateToReunionesEdit(reunion.get('id')));
    },

    mostrarSolicitudParaReabrirReunion(){
      this.set('modalDeReabrirReunionAbierto', true);
    },

    compartirReunion(reunion) {
      const baseUrl = window.location.host;
      const minutaUrl = baseUrl + "/minuta/" + reunion.id + "/ver";
      const reunionUrl = baseUrl + '/reuniones/reuniones/' + reunion.id;

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

  _reabrirVotacion(reunion){
    return this.reunionService().reabrirReunion(reunion);
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

import Ember from 'ember';
import NavigatorInjected from "../mixins/navigator-injected";
import DuracionesServiceInjected from "../mixins/duraciones-service-injected";
import ReunionServiceInjected from "../mixins/reunion-service-injected";

export default Ember.Component.extend(NavigatorInjected, ReunionServiceInjected, DuracionesServiceInjected, {
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

    eliminarReunion(reunion) {
      this.reunionService().removeReunion(reunion)
        .then(() => this.recargarLista());
    },

    compartirReunion(reunion) {
      const baseUrl = window.location.host;
      const linkToShare = baseUrl + "/minuta/" + reunion.id + "/ver";
      navigator.clipboard.writeText(linkToShare)
        .then(() => {
          this.set('showCopyToClipboardMessage', true);
          setTimeout(() => this.set('showCopyToClipboardMessage', false), 1000);
        }).catch(() => console.error("No se pudo copiar"));
    },

    editarReunion(reunion) {
      this.navigator().navigateToReunionesEdit(reunion.get('id'));
    },

  },

  _obtenerDuracionDeTema(unTema) {
    var duraciones = this.get('duraciones');
    return duraciones.find(function (duracion) {
      return duracion.nombre === unTema.duracion;
    });
  },
});

import Ember from 'ember';
import NavigatorInjected from "../mixins/navigator-injected";
import ReunionServiceInjected from "../mixins/reunion-service-injected";

export default Ember.Component.extend(NavigatorInjected, ReunionServiceInjected, {

  tableTitles: Ember.computed('reunionCerrada', function () {
    const titulosDefault = '#, Título, Autor, Duración, Votos';
    const titulosConTemasTratados = titulosDefault + ', Tratado';
    return this.get('reunionCerrada') ? titulosConTemasTratados : titulosDefault;
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
});

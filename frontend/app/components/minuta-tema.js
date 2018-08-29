import Ember from 'ember';
import MinutaServiceInjected from "../mixins/minuta-service-injected";
import TemaDeMinutaServiceInjected from "../mixins/tema-de-minuta-service-injected";
import NavigatorInjected from "../mixins/navigator-injected";
import UserServiceInjected from "../mixins/user-service-injected";

export default Ember.Component.extend(MinutaServiceInjected, TemaDeMinutaServiceInjected, NavigatorInjected, UserServiceInjected, {

  actions: {
    verEditorDeConclusion(tema) {
      this._mostrarEditorDeConclusion(tema);
    },

    cerrarEditor() {
      this._ocultarEditor();
    },

      guardarConclusion(fueTratado) {
        debugger;
        var tema = this.get('temaDeMinuta');
        tema.actionItems.forEach((actionItem) => {
          delete actionItem.usuarios;
        delete actionItem.usuariosSeleccionables;
      });

      tema.set('fueTratado', fueTratado);

      this.temaDeMinutaService().updateTemaDeMinuta(tema)
        .then(() => {
          this._recargarLista();

          this._ocultarEditor();
        });
    }
  },
  _mostrarEditorDeConclusion(tema) {
    var indiceClickeado = this.get('model.minuta.temas').indexOf(tema);
    this.set('indiceSeleccionado', indiceClickeado);
    this._mostrarEditor();
  },

  _mostrarEditor() {
    this.set('mostrandoEditor', true);
  },

  _ocultarEditor() {
    this.set('indiceSeleccionado', null);
    this.set('mostrandoEditor', false);
    this.set('anchoDeTabla', 's12');
  }
});

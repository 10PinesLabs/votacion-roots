import Ember from 'ember';

export default Ember.Component.extend({

  actions: {
    verEditorDeConclusion(tema) {
      this._mostrarEditorDeConclusion(tema);
    },

    cerrarEditor() {
      this._ocultarEditor();
    },

    guardarConclusion(fueTratado) {
      var tema = this.get('temaAEditar');
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

import Ember from 'ember';
import MinutaServiceInjected from "../mixins/minuta-service-injected";
import TemaDeMinutaServiceInjected from "../mixins/tema-de-minuta-service-injected";
import NavigatorInjected from "../mixins/navigator-injected";
import UserServiceInjected from "../mixins/user-service-injected";

export default Ember.Component.extend(MinutaServiceInjected, TemaDeMinutaServiceInjected, NavigatorInjected, UserServiceInjected, {

  btnColorSi: Ember.computed('temaDeMinuta.fueTratado', function(){
    if(this.get('temaDeMinuta.fueTratado')){
      return "btn";
    }else{
      return "";
    }
  }),

 btnColorNo: Ember.computed('btnColorSi', function(){
    if(!this.get('btnColorSi')){
      return "btn";
    }else{
      return "";
    }
  }),

  actions: {
    verEditorDeConclusion(tema) {
      this._mostrarEditorDeConclusion(tema);
    },

    cerrarEditor() {
      this._ocultarEditor();
    },

    setTratado(fueTratado){
      this.set('temaDeMinuta.fueTratado', fueTratado);
      this._mostrarEditor();
    },

    guardarConclusion() {
      var tema = this.get('temaDeMinuta');
      tema.actionItems.forEach((actionItem) => {
        delete actionItem.usuarios;
        delete actionItem.usuariosSeleccionables;
      });
      this._updateTema(tema);
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
  },
  _recargarLista() {
    this.get('router').refresh();
  },

  _updateTema(tema) {
    this._ocultarEditor();
    this.temaDeMinutaService().updateTemaDeMinuta(tema)
      .then((response) => {
        this._mostrarUsuariosSinMail(response);
      }, (error) => {
        this._recargarLista();
      });
  },
});

import Ember from 'ember';
import MinutaServiceInjected from "../mixins/minuta-service-injected";
import TemaDeMinutaServiceInjected from "../mixins/tema-de-minuta-service-injected";
import NavigatorInjected from "../mixins/navigator-injected";
import UserServiceInjected from "../mixins/user-service-injected";

export default Ember.Component.extend(MinutaServiceInjected, TemaDeMinutaServiceInjected, NavigatorInjected, UserServiceInjected, {

  mostrandoToastUsuariosSinMail: false,

  minutaParagraph: Ember.computed('mostrandoEditor', function () {
    if (!this.get('mostrandoEditor')) {
      return "minuta-paragraph";
    }
    return "";
  }),

  actions: {
    verEditorDeConclusion(tema) {
      this._mostrarEditorDeConclusion(tema);
    },

    cerrarEditor() {
      this._ocultarEditor();
    },
    
    mostrarEditor(){
      this._mostrarEditor();
    },
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
    this.temaDeMinutaService().updateTemaDeMinuta(tema)
      .then((response) => {
        this._ocultarEditor();
        this._mostrarUsuariosSinMail(response);
      }, (error) => {
        this._recargarLista();
      });
  },

  _mostrarUsuariosSinMail(response) {
    let nombresDePersonasSinMailConRepetidos = [].concat.apply([],
      response.actionItems.map(actionItem => actionItem.responsables)
    )
      .filter(user => user.mail === undefined || user.mail === "" || user.mail === null)
      .map(resp => resp.name);

    let nombresDePersonasSinMailSinRepetidos =
      nombresDePersonasSinMailConRepetidos
        .filter(function (elem, pos) {
          return nombresDePersonasSinMailConRepetidos.indexOf(elem) === pos;
        });

    if (nombresDePersonasSinMailSinRepetidos.length !== 0) {
      this.set('nombresDePersonasSinMail', nombresDePersonasSinMailSinRepetidos);
      this.mostrar_alerta_por_falta_de_mail();
    }
  },

  mostrar_alerta_por_falta_de_mail() {
    this.set('mostrandoToastUsuariosSinMail', true);
    setTimeout(() => {
      this.set('mostrandoToastUsuariosSinMail', false);
    }, 4000);
  },
});

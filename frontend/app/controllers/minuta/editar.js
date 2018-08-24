import Ember from "ember";
import MinutaServiceInjected from "../../mixins/minuta-service-injected";
import NavigatorInjected from "../../mixins/navigator-injected";
import TemaDeMinutaServiceInjected from "../../mixins/tema-de-minuta-service-injected";
import UserServiceInjected from "../../mixins/user-service-injected";

export default Ember.Controller.extend(MinutaServiceInjected, TemaDeMinutaServiceInjected, NavigatorInjected, UserServiceInjected, {

  nombresDePersonasSinMail: "",

  reunionId: Ember.computed('model.reunionId', function () {
    return this.get('model.reunionId');
  }),

  minuta: Ember.computed('model.minuta', function () {
    return this.get('model.minuta');
  }),

  usuariosSeleccionables: Ember.computed('model.usuarios', 'usuariosSeleccionados', function () {
    var todosLosUsuarios = this.get('model.usuarios');
    var usuariosSeleccionados = this.get('usuariosSeleccionados');
    return todosLosUsuarios.filter(function (usuario) {
      return !usuariosSeleccionados.some(function (seleccionado) {
        return usuario.id === seleccionado.id;
      });
    });
  }),

  usuariosSeleccionados: Ember.computed('model.votantes', function () {
    return this.get('model.votantes');
  }),

  temaAEditar: Ember.computed('temaSeleccionado', function () {
    let tema = this.get('temaSeleccionado');
    let actionItems = [];
    this.get('temaSeleccionado.actionItems').forEach((actionItem) => actionItems.push(actionItem));
    return Ember.Object.extend().create({
      id: tema.id,
      idDeMinuta: tema.idDeMinuta,
      tema: tema.tema,
      conclusion: tema.conclusion,
      fueTratado: tema.fueTratado,
      actionItems: actionItems
    });
  }),

  actions: {
    guardarUsuariosSeleccionados() {
      this.set('model.minuta.asistentes', this.get('usuariosSeleccionados'));
      this.minutaService().updateMinuta(this.get('model.minuta'));
    },

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
        .then((response) => {
          this._mostrarUsuariosSinMail(response);
          this._recargarLista();
          this._ocultarEditor();
        });
    },
  },

  anchoDeTabla: 's12',

  temaSeleccionado: Ember.computed('minuta', 'indiceSeleccionado', function () {
    var indiceSeleccionado = this.get('indiceSeleccionado');
    var temas = this.get('minuta.temas');

    return temas.objectAt(indiceSeleccionado);
  }),

  _mostrarEditorDeConclusion(tema) {
    var indiceClickeado = this.get('minuta.temas').indexOf(tema);
    this.set('indiceSeleccionado', indiceClickeado);
    this._mostrarEditor();
  },

  _mostrarEditor() {
    this.set('anchoDeTabla', 's4');
    this.set('mostrandoEditor', true);
  },

  _ocultarEditor() {
    this.set('indiceSeleccionado', null);
    this.set('mostrandoEditor', false);
    this.set('anchoDeTabla', 's12');
  },

  _mostrarUsuariosSinMail(response) {
    let nombresDePersonasSinMail = response.actionItems.map(actionItem => actionItem.responsables)
      .filter(user => user.mail === null)
      .map(resp => resp.name);

    if (nombresDePersonasSinMail.length !== 0) {
      this.set('nombresDePersonasSinMail', nombresDePersonasSinMail);
      var x = document.getElementById("toast");
      x.className = "show";
      setTimeout(function () {
        x.className = x.className.replace("show", "");
      }, 5000);
    }
  },

  _recargarLista() {
    this.get('target.router').refresh();
  }
});

import Ember from "ember";
import MinutaServiceInjected from "../../mixins/minuta-service-injected";
import NavigatorInjected from "../../mixins/navigator-injected";
import TemaDeMinutaServiceInjected from "../../mixins/tema-de-minuta-service-injected";
import UserServiceInjected from "../../mixins/user-service-injected";

export default Ember.Controller.extend(MinutaServiceInjected, TemaDeMinutaServiceInjected, NavigatorInjected, UserServiceInjected, {

  mostrandoToastUsuariosSinMail: false,

  temasPendientes: Ember.computed('model.minuta', function () {
    return  this.get('model.minuta').temas.filter(tema => !tema.fueTratado);
  }),

  temasTratados: Ember.computed('model.minuta', function () {
    return  this.get('model.minuta').temas.filter(tema => tema.fueTratado);
  }),

  router: Ember.computed('target.router', function () {
    return this.get('target.router');
  }),

  fueTratado: Ember.computed('temaDeMinuta.conclusion', function () {
    return !!(this.get('temaDeMinuta.conclusion'));
  }),


  reunionId: Ember.computed('model.reunionId', function () {
    return this.get('model.reunionId');
  }),

  minuta: Ember.computed('model.minuta', function () {
    return this.get('model.minuta');
  }),

  fecha: Ember.computed('model.minuta', function () {
    return moment(this.get('model.minuta').fecha).format('DD-MM-YYYY');
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

  usuariosSeleccionados: Ember.computed('model.minuta.asistentes', function () {
    return this.get('model.minuta.asistentes');
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

  mostrarEditor: Ember.computed('mostrandoEditor', function () {
    return "hidden";
  }),

  actions: {

    verEditorDeConclusion(tema) {
      this._mostrarEditorDeConclusion(tema);
    },

    cerrarEditor() {
      this._ocultarEditor();
    },

    guardarConclusion(tema) {
      tema.actionItems.forEach((actionItem) => {
        delete actionItem.usuarios;
        delete actionItem.usuariosSeleccionables;
      });

      this.temaDeMinutaService().updateTemaDeMinuta(tema)
        .then((response) => {
          this._mostrarUsuariosSinMail(response);
          this._ocultarEditor();
          this._recargarLista();
        }, (error) => {
        this._recargarLista();
      });
    },

    quitarAsistente(usuario) {
      this.agregarUsuarioAYSacarUsuarioDe(usuario, 'usuariosSeleccionables', 'usuariosSeleccionados');

      this.guardarUsuariosSeleccionados();
    },

    agregarAsistente(usuario) {
      this.agregarUsuarioAYSacarUsuarioDe(usuario, 'usuariosSeleccionados', 'usuariosSeleccionables');

      this.guardarUsuariosSeleccionados();
    },
  },

    guardarUsuariosSeleccionados() {
      this.set('model.minuta.asistentes', this.get('usuariosSeleccionados'));
      this.minutaService().updateMinuta(this.get('model.minuta'));
    },

    agregarUsuarioAYSacarUsuarioDe(usuario, nombreListaDestino, nombreListaOrigen) {
      let listaDestino = this.get(nombreListaDestino);
      listaDestino.pushObject(usuario);
      this.set(nombreListaDestino, listaDestino);

      let listaOrigen = this.get(nombreListaOrigen);
      listaOrigen.removeObject(usuario);
      this.set(nombreListaOrigen, listaOrigen);
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

  _recargarLista() {
    this.get('target.router').refresh();
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

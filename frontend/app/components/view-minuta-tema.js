import Ember from 'ember';
import MinutaServiceInjected from "../mixins/minuta-service-injected";
import TemaDeMinutaServiceInjected from "../mixins/tema-de-minuta-service-injected";
import NavigatorInjected from "../mixins/navigator-injected";
import UserServiceInjected from "../mixins/user-service-injected";

export default Ember.Component.extend(MinutaServiceInjected, TemaDeMinutaServiceInjected, NavigatorInjected, UserServiceInjected, {

  mostrarDetalle: Ember.computed('mostrarDetalle', function () {
    return false;
  }),

  mostrarBotonAdd:Ember.computed('mostrarBotonAdd', function(){
    var agregarItem = this.get('agregarItem');
    if(agregarItem){
      return "hidden";
    }else{
      return "";
    }
  }),

  agregarItem: Ember.computed('agregarItem', function(){
    return false;
  }),

  usuarios: Ember.computed('model.usuarios', function(){
    return this.get('model.usuarios');
  }),

  actions: {
    verDetalleDeTema(tema){
      return this._mostrarDetalle(tema);
    },

    ocultarDetalleDeTema(tema){
      return this._ocultarDetalle(tema);
    },

    agregarActionItem(){
      this._agregarNuevoActionItem();
    },

    ocultarAgregadoActionItem(){
      this.set('agregarItem',false);
    },

    soloGuardar(tema){
      this._guardar(tema);
    },

    guardarYCrearOtro(tema){
      this._guardar(tema);
      this._agregarNuevoActionItem();
    },

  },
  _mostrarDetalle(tema){
    var indiceClickeado = this.get('model.minuta.temas').indexOf(tema);
    this.set('indiceSeleccionado', indiceClickeado);
    this.set('mostrarDetalle', true);
  },

  _ocultarDetalle(tema){
    var indiceClickeado = this.get('model.minuta.temas').indexOf(tema);
    this.set('indiceSeleccionado', indiceClickeado);
    this.set('mostrarDetalle', false);
  },

  _guardar(tema){
    tema.actionItems.forEach((actionItem) => {
      delete actionItem.usuarios;
      delete actionItem.usuariosSeleccionables;
    });

    this.temaDeMinutaService().updateTemaDeMinuta(tema)
      .then((response) => {
        this._mostrarUsuariosSinMail(response);
        this._recargarLista();
        this._ocultarEditor();
      });
  },

  _agregarNuevoActionItem(){
    this.get('temaDeMinuta').actionItems.pushObject(
      Ember.Object.extend().create({
        descripcion: "",
        responsables: [],
      }));
    this.set('agregarItem',true);
    this.rerender();
  }
});

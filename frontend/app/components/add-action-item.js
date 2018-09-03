import Ember from 'ember';
import NavigatorInjected from "../mixins/navigator-injected";
import UserServiceInjected from "../mixins/user-service-injected";
import MinutaServiceInjected from "../mixins/minuta-service-injected";
import TemaDeMinutaServiceInjected from "../mixins/tema-de-minuta-service-injected";

export default Ember.Component.extend(MinutaServiceInjected, TemaDeMinutaServiceInjected, NavigatorInjected, UserServiceInjected,{

  usuarios: Ember.computed('model.usuarios', function(){
    return this.get('model.usuarios');
  }),

  guardarHabilitado: Ember.computed('actionItemEnCreacion.descripcion', 'actionItemEnCreacion.responsables', function () {
    var descripcion = this.get('actionItemEnCreacion.descripcion');
    var responsables = this.get('actionItemEnCreacion.responsables');
      if (!descripcion || responsables.length <= 0) {
        return "disabled";
      }
      else {
        return "";
      }
    }),

  init() {
    this._super(...arguments);
    this._setActionItemVacio();
  },

  actions:{
    limpiarDespuesDe(queHacer){
      queHacer();
      this._setActionItemVacio();
    }
  },

  _setActionItemVacio: function () {
    this.set('actionItemEnCreacion', Ember.Object.extend().create({
      descripcion: "",
      responsables: [],
    }));
  },

});

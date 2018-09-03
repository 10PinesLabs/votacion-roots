import Ember from 'ember';
import NavigatorInjected from "../mixins/navigator-injected";
import UserServiceInjected from "../mixins/user-service-injected";
import MinutaServiceInjected from "../mixins/minuta-service-injected";
import TemaDeMinutaServiceInjected from "../mixins/tema-de-minuta-service-injected";

export default Ember.Component.extend(MinutaServiceInjected, TemaDeMinutaServiceInjected, NavigatorInjected, UserServiceInjected,{

  usuarios: Ember.computed('model.usuarios', 'responsables', function(){
    return this.get('model.usuarios');
  }),

  _setActionItemVacio: function () {
    this.set('actionItemEnCreacion', Ember.Object.extend().create({
      descripcion: "",
      responsables: [],
    }));
  },

  init() {
    this._super(...arguments);
    this._setActionItemVacio();
  },

  actions:{
    limpiarDespuesDe(queHacer){
      queHacer();
      this._setActionItemVacio();
    }
  }

});

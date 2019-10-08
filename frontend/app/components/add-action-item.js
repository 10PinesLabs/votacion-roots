import Ember from 'ember';
import NavigatorInjected from "../mixins/navigator-injected";
import UserServiceInjected from "../mixins/user-service-injected";
import MinutaServiceInjected from "../mixins/minuta-service-injected";
import TemaDeMinutaServiceInjected from "../mixins/tema-de-minuta-service-injected";

export default Ember.Component.extend(MinutaServiceInjected, TemaDeMinutaServiceInjected, NavigatorInjected, UserServiceInjected, {
  classNames: ['card-action mb-20'],

  init() {
    this._super(...arguments);
    this._setActionItemVacio();
  },

  actions: {
    limpiarDespuesDe(queHacer) {
      queHacer();
      this._setActionItemVacio();
    },
    agregarActionItem() {
      this.get('temaDeMinuta').actionItems.pushObject(
        Ember.Object.extend().create({
          descripcion: "",
          responsables: [],
        }));
      this.rerender();
    },
  },

  _setActionItemVacio: function () {
    this.set('actionItemEnCreacion', Ember.Object.extend().create({
      descripcion: "",
      responsables: [],
    }));
  },

});



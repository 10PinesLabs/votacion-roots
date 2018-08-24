import Ember from "ember";

export default Ember.Component.extend({
  iteraciones: Ember.computed('veces', function () {
    var veces = this.get('veces');
    var iteraciones = [];
    for (var i = 0; i < veces; i++) {
      iteraciones.push(i);
      this.set('index', i);
    }
    return iteraciones;
  }),

  index: Ember.computed('index', function () {
    return this.get('index');
  }),

  actions: {
    fueVotado(tema) {
      debugger;
      return 'vot-star';
    },

    getTema(tema) {
      debugger;
      return "";
    }
  }
});

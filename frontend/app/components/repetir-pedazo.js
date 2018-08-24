import Ember from "ember";

export default Ember.Component.extend({
  iteraciones: Ember.computed('veces', function () {
    var veces = this.get('veces');
    var iteraciones = [];
    for (var i = 0; i < veces; i++) {
      iteraciones.push(i);
    }
    return iteraciones;
  }),
});

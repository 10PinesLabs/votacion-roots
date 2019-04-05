import Ember from 'ember';

export default Ember.Component.extend({

  tipoDeTexto: Ember.computed('extendido', function () {
    if(this.get('extendido')) {
      return "descripcion-expandida";
    } else {
      return "descripcion-no-expandida";
    }
  }),
});

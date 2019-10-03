import Ember from 'ember';

export default Ember.Component.extend({
  antiguedad: Ember.computed('tema', 'antiguedadRelativa', function(){
    if (this.get('antiguedadRelativa')){
      return this.tema.get('antiguedadDePropuesta');
    } else {
      return "el " + this.tema.get('fechaDePropuestaOriginal');
    }
  })
});

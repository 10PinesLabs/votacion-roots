import Ember from 'ember';

export default Ember.Component.extend({
  antiguedad: Ember.computed('tema', 'antiguedadRelativa', function(){
    if (this.get('antiguedadRelativa')){
      return "hace" + this.tema.antiguedadDePropuesta;
    } else {
      return "el " + this.tema.get('fechaDePrimeraPropuesta');
    }
  })
});
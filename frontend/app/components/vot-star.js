import Ember from 'ember';

export default Ember.Component.extend({

  primerVoto: Ember.computed('reunion','temasVotados', function() {
    return this.temasVotados[0];
  }),

  segundoVoto: Ember.computed('reunion','temasVotados', function() {
    return this.temasVotados[1];
  }),

  tercerVoto: Ember.computed('reunion.tema','temasVotados', function() {
    return this.temasVotados[2];
  }),

});


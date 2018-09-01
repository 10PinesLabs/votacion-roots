import Ember from 'ember';

export default Ember.Component.extend({

  primerVoto: Ember.computed('updatingVotos','temasVotados', function() {
    return this.temasVotados[0];
  }),

  segundoVoto: Ember.computed('updatingVotos','temasVotados', function() {
    return this.temasVotados[1];
  }),

  tercerVoto: Ember.computed('updatingVotos','temasVotados', function() {
    return this.temasVotados[2];
  }),

});


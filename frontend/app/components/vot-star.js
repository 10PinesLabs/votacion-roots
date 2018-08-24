import Ember from 'ember';

export default Ember.Component.extend({

  primerTema: Ember.computed('temasVotados', function () {
    return this.get('temasVotados')[0];
  }),

  segundoTema: Ember.computed('temasVotados', function () {
    return this.get('temasVotados')[1];
  }),

  tercerTema: Ember.computed('temasVotados', function () {
    return this.get('temasVotados')[2];
  }),

});


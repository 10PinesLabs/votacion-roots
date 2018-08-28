import Ember from 'ember';

export default Ember.Component.extend({
  tieneVoto:
    Ember.computed('tema', function () {
      return this.get('tema') != null;
    }),
});

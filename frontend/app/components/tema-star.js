import Ember from 'ember';

export default Ember.Component.extend({

  votStyle: Ember.computed('tema', function () {
      if (this.get('tieneVoto')) {
        return 'vot-star';
      }
      return 'star';
    }
  ),

  tieneVoto:
    Ember.computed('tema', function () {
      return this.get('tema') != undefined;
    }),

  color:
    Ember.computed('tema', function () {
      if (this.get('tieneVoto')) {
        return 'yellow-star';
      }
      return 'grey-text';
    }),

  titulo:
    Ember.computed('tema', function () {
      if(this.get('tieneVoto')) {
        return this.get('tema').titulo;
      }
      return "";
    }),

});

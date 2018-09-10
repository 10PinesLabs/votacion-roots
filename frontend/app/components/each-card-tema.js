import Ember from 'ember';

export default Ember.Component.extend({

  expandido: false,

  actions:
    {
      expandirDescripcion(){
        this.set('expandido', true);
      },

      colapsarDescripcion(){
        this.set('expandido', false);
      },
    }
});

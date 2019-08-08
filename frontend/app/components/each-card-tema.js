import Ember from 'ember';

export default Ember.Component.extend({

  expandido: false,
  esTemaDeProponerPino: Ember.computed('tema', function(){
    return this.get('tema.tipo') === "proponerPinos";
  }),
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

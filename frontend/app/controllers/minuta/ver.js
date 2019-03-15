import Ember from 'ember';

export default Ember.Controller.extend({

  minuta: Ember.computed('model.minuta', function () {
    return this.get('model.minuta');
  }),

  reunionId: Ember.computed('model.reunionId', function () {
    return this.get('model.reunionId');
  }),

  fecha: Ember.computed('model.minuta', function () {
    return moment(this.get('model.minuta').fecha).format('DD-MM-YYYY');
  }),

  temasTratados: Ember.computed('minuta.temas', function () {
    return this.get('minuta.temas').filter((tema) => tema.fueTratado);
  }),

  temasNoTratados: Ember.computed('minuta.temas', function () {
    return this.get('minuta.temas').filter((tema) => !tema.fueTratado);
  }),

});

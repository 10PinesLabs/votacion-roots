import Ember from 'ember';

export default Ember.Component.extend({
  btnColorSi: Ember.computed('tema.fueTratado', function () {
    if (this.get('tema.fueTratado')) {
      return "btn";
    } else {
      return "";
    }
  }),

  btnColorNo: Ember.computed('btnColorSi', function () {
    if (!this.get('btnColorSi')) {
      return "btn";
    } else {
      return "";
    }
  }),


  actions: {
    tratar(fueTratado) {
      this.set('tema.fueTratado', fueTratado);
    }
  }

});

import Ember from 'ember';

export default Ember.Component.extend({

  actions: {
    agregarActionItem() {
      this.get('temaDeMinuta').actionItems.pushObject(
        Ember.Object.extend().create({
          descripcion: "",
          responsables: [],
        }));
      this.rerender();
    },
  },
});

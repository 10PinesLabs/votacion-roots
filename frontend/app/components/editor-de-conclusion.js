import Ember from 'ember';

export default Ember.Component.extend({

  guardarConclusionHabilitado: Ember.computed('temaDeMinuta.actionItems.@each.descripcion',
    'temaDeMinuta.actionItems.@each.responsables',
    'temaDeMinuta.actionItems.[]', function () {
      if (this.get('temaDeMinuta.actionItems').some((actionItem) => {
        return !actionItem.descripcion || actionItem.responsables.length <= 0;
      })) {
        return "disabled";
      }
      else {
        return "";
      }
    }),
  actions: {
    agregarActionItem() {
      this.get('temaDeMinuta').actionItems.pushObject(
        Ember.Object.extend().create({
          descripcion: "",
          responsables: [],
        }));
      this.rerender();
    },
<<<<<<< HEAD

  },
});
=======
    },
  });
>>>>>>> 9b169c1cf06f1606f5b028c41dd3f2d439b60e26

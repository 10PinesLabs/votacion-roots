import Ember from 'ember';

export default Ember.Component.extend({
  classNames: ["minuta-botom-tema"],

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
    })
});

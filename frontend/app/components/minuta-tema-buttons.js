import Ember from 'ember';

export default Ember.Component.extend({
  classNames: ["minuta-botom-tema"],

  guardarHabilitado: Ember.computed('temaDeMinuta.actionItems.@each.descripcion',
    'temaDeMinuta.actionItems.@each.responsables',
    'temaDeMinuta.actionItems.[]', function () {
      return this.get('temaDeMinuta.actionItems').every((actionItem) => {
        return actionItem.descripcion && actionItem.responsables.length > 0;
      })
    })
});

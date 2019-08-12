import Ember from 'ember';

export default Ember.Component.extend({
  didInsertElement() {
    document.getElementById("pinoPropuesto").focus();
  },
  guardarHabilitado: Ember.computed('pino', 'guardando', function () {
    return this.get('pino') && !this.get('guardando')
  }),
  actions:
    {
      guardar(funcionGuardadora) {
        this.set('guardando', true);
        funcionGuardadora(this.get('pino'));
      }
    }
});

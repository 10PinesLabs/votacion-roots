import Ember from 'ember';

export default Ember.Component.extend({
  guardarHabilitado: Ember.computed('pino', function () {
    return !this.get('pino') || this.get('guardando')
  }),
  actions:
    {
      guardar(funcionGuardadora) {
        this.set('guardando', true);
        funcionGuardadora(this.get('pino'));
      }
    }
});

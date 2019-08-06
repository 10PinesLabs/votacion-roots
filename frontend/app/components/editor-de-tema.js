import Ember from "ember";

export default Ember.Component.extend({

  guardando: false,

  didRender() {
    this._super(...arguments);
    $('select').material_select();
  },

  didInsertElement() {
    this.$('#titulo').focus();
  },

  guardarHabilitado: Ember.computed('tema.duracion', 'tema.titulo', 'guardando',
    function () {
      return !this.get('tema.duracion') || !this.get('tema.titulo') || this.get('guardando')
    }
  ),

  actions:
    {
      guardar(funcionGuardadora) {
        this.set('guardando', true);
        funcionGuardadora();
      }

    }
});

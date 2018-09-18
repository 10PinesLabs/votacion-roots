import Ember from "ember";

export default Ember.Component.extend({

  didRender() {
    this._super(...arguments);
    $('select').material_select();
  },

  didInsertElement() {
    this.$('#titulo').focus();
  },

  guardarHabilitado: Ember.computed('tema.duracion', 'tema.titulo','tema.actionItems', function () {
    if (!this.get('tema.duracion') || !this.get('tema.titulo') ) {
      return "disabled";
    }
    else {
      return "";
    }
  }),

  actions:
    {
      guardar(funcionGuardadora){
        if (this.get('tema.duracion') && this.get('tema.titulo') ) {
          funcionGuardadora.call();
        }
      }
    }
});

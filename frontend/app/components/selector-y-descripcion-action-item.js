import Ember from 'ember';

export default Ember.Component.extend({
  usuariosSeleccionables:Ember.computed('actionItem.responsables', function () {
    var todosLosUsuarios = this.get('usuariosParaListar');
    var usuariosSeleccionados = this.get('actionItem.responsables');
    return todosLosUsuarios.filter((usuario) =>
      !usuariosSeleccionados.some((seleccionado) =>
         usuario.id === seleccionado.id
      ));
  }),
});

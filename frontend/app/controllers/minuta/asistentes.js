import Ember from "ember";
import MinutaServiceInjected from "../../mixins/minuta-service-injected";
import NavigatorInjected from "../../mixins/navigator-injected";

export default Ember.Controller.extend(MinutaServiceInjected, NavigatorInjected, {

  reunionId: Ember.computed('model.reunionId', function () {
    return this.get('model.reunionId');
  }),

  nombresUsuariosSeleccionables: Ember.computed('model.usuarios', 'nombresUsuariosSeleccionados', function () {
    var todosLosUsuarios = this.get('model.usuarios');
    var usuariosSeleccionados = this.get('nombresUsuariosSeleccionados');
    return todosLosUsuarios.filter(function (usuario) {
      return !usuariosSeleccionados.some(function(seleccionado){
        return usuario.id === seleccionado.id;
      });
    }).map(function(usuario){
      return usuario.name});
  }),

  nombresUsuariosSeleccionados: Ember.computed('model.minuta.asistentes', function() {
    return this.get('model.minuta.asistentes').map(function(usuario){return usuario.name});
  }),
  usuariosSeleccionados: Ember.computed('model.minuta.asistentes', function() {
    var self = this;
    return this.get('model.usuarios').filter(function(usuario){
      return self.get('nombresUsuariosSeleccionados').any(function(nombre){
        return usuario.name === nombre;
      })
    });
  }),
  actions: {
    guardarUsuariosSeleccionadosYContinuar(){
      let reunionId = this.get('reunionId');
      this.set('model.minuta.asistentes', this.get('usuariosSeleccionados'));
      this.minutaService().updateMinuta(this.get('model.minuta')).then(() => {
        this.navigator().navigateToConclusiones(reunionId);
      });
    }
  }
});

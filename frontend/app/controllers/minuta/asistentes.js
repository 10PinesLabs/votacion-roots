import Ember from "ember";
import MinutaServiceInjected from "../../mixins/minuta-service-injected";
import NavigatorInjected from "../../mixins/navigator-injected";

export default Ember.Controller.extend(MinutaServiceInjected, NavigatorInjected, {

  reunionId: Ember.computed('model.reunionId', function () {
    return this.get('model.reunionId');
  }),

  minuta:Ember.computed('model.minuta',function(){
    return this.get('model.minuta');
  }),

  usuariosSeleccionables: Ember.computed('model.usuarios', 'usuariosSeleccionados', function () {
    var todosLosUsuarios = this.get('model.usuarios');
    var usuariosSeleccionados = this.get('usuariosSeleccionados');
    return todosLosUsuarios.filter(function (usuario) {
      return !usuariosSeleccionados.some(function(seleccionado){
        return usuario.id === seleccionado.id;
      });
    });
  }),

  usuariosSeleccionados: Ember.computed('model.minuta.asistentes', function() {
    return this.get('model.minuta.asistentes');
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

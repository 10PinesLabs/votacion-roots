import Ember from "ember";
import MinutaServiceInjected from "../../mixins/minuta-service-injected";
import NavigatorInjected from "../../mixins/navigator-injected";
import UserServiceInjected from "../../mixins/user-service-injected";

export default Ember.Controller.extend(MinutaServiceInjected, NavigatorInjected, UserServiceInjected,{

  reunionId: Ember.computed('model.reunionId', function () {
    return this.get('model.reunionId');
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

  usuariosSeleccionados: Ember.computed('model.votantes', function() {
    return this.get('model.votantes');
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

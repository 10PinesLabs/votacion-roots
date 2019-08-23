import Ember from "ember";
import AuthenticatedRoute from "ateam-ember-authenticator/mixins/authenticated-route";
import NavigatorInjected from "../../mixins/navigator-injected";
import ReunionServiceInjected from "../../mixins/reunion-service-injected";
import TemaServiceInjected from "../../mixins/tema-service-injected";
import UserServiceInjected from "../../mixins/user-service-injected";
import Tema from "../../concepts/tema";

export default Ember.Route.extend(AuthenticatedRoute, ReunionServiceInjected, UserServiceInjected, TemaServiceInjected, NavigatorInjected, {
  queryParams:{
    temaAReproponer: {
      refreshModel: true
    }
  },
  model: function (params) {
    const requests = {
      reunion: this.promiseWaitingFor(this.reunionService().getReunion(params.reunion_id))
        .whenInterruptedAndReauthenticated(() => {
          this.navigator().navigateToReunionesEdit(params.reunion_id);
        }),
      usuarioActual: this.promiseWaitingFor(this.userService().getCurrentUser())
        .whenInterruptedAndReauthenticated(() => {
          this.navigator().navigateToReunionesEdit(params.reunion_id);
        })
    };

    if (params.temaAReproponer) {
      requests.temaAReproponer = this.promiseWaitingFor(this.temaService().getTema(params.temaAReproponer))
        .whenInterruptedAndReauthenticated(() => {
          this.navigator().navigateToReunionesEdit(params.reunion_id, params);
        });
    }

    return Ember.RSVP.hash(requests).then((model) => {
      this._usarInstanciasDeTemas(model.reunion, model.usuarioActual);
      return model;
    });
  },

  _usarInstanciasDeTemas(reunion, usuarioActual){
    var temasDeLaReunion = reunion.get('temasPropuestos');
    for (var i = 0; i < temasDeLaReunion.length; i++) {
      var temaDeLaReunion = temasDeLaReunion[i];
      temaDeLaReunion.set('usuarioActual', usuarioActual);
      var temaConComportamiento = Tema.create(temaDeLaReunion);
      temasDeLaReunion[i] = temaConComportamiento;
    }
  },

  setupController: function(controller, model) {
    this._super(controller, model);
    if (model.temaAReproponer) {
      controller.send('mostrarFormularioDeReproponer', model.temaAReproponer)
    }
  }

});

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
    const reunionId = params.reunion_id;

    return Ember.RSVP.hashSettled({
      reunion: this.promiseWaitingFor(this.reunionService().getReunion(reunionId))
        .whenInterruptedAndReauthenticated(()=> {
          this.navigator().navigateToReunionesEdit(reunionId);
        }),
      usuarioActual: this.promiseWaitingFor(this.userService().getCurrentUser())
        .whenInterruptedAndReauthenticated(()=> {
          this.navigator().navigateToReunionesEdit(reunionId);
        }),
      temaParaReproponer: this.promiseWaitingFor(this.temaService().getTema(params.temaAReproponer))
        .whenInterruptedAndReauthenticated(() => {
          this.navigator().navigateToReunionesEdit(reunionId, params);
        }),
  }).then((responses)=> {
      this._alertIfTemaNoEncontrado(params.reproponer, responses.temaParaReproponer);

      let model = {};
      Object.keys(responses).forEach((key) => model[key] = responses[key].value);

      this._usarInstanciasDeTemas(model.reunion, model.usuarioActual);
      return model;
    });
  },

  _alertIfTemaNoEncontrado(param, result) {
    if (param !== undefined && result.state === "rejected") {
      alert("El tema no fue encontrado")
    }
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
    controller.send('mostrarFormularioDeReproponer', model.temaParaReproponer)
  }

});

import Ember from "ember";
import AuthenticatedRoute from "ateam-ember-authenticator/mixins/authenticated-route";
import NavigatorInjected from "../../mixins/navigator-injected";
import ReunionServiceInjected from "../../mixins/reunion-service-injected";
import UserServiceInjected from "../../mixins/user-service-injected";
import {temasConComportamiento} from "../../concepts/tema";

export default Ember.Route.extend(AuthenticatedRoute, ReunionServiceInjected, UserServiceInjected, NavigatorInjected, {
  model: function (params) {
    var reunionId = params.reunion_id;

    return Ember.RSVP.hash({
      reunion: this.promiseWaitingFor(this.reunionService().getReunion(reunionId))
        .whenInterruptedAndReauthenticated(()=> {
          this.navigator().navigateToReunionesEdit(reunionId);
        }),
      usuarioActual: this.promiseWaitingFor(this.userService().getCurrentUser())
        .whenInterruptedAndReauthenticated(()=> {
          this.navigator().navigateToReunionesEdit(reunionId);
        })
    }).then((model)=> {
      model.reunion.set('temasPropuestos',
        temasConComportamiento(model.reunion.get('temasPropuestos'), model.usuarioActual)
      );
      return model;
    });
  },
});

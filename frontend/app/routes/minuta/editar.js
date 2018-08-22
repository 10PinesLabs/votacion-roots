import Ember from 'ember';
import MinutaServiceInjected from "../../mixins/minuta-service-injected";
import NavigatorInjected from "../../mixins/navigator-injected";
import UserServiceInjected from "../../mixins/user-service-injected";
import AuthenticatedRoute from "ateam-ember-authenticator/mixins/authenticated-route";

export default Ember.Route.extend(MinutaServiceInjected, UserServiceInjected, AuthenticatedRoute, NavigatorInjected, {
  model: function (params) {
    var reunionId = params.reunion_id;

    return Ember.RSVP.hash({
      reunionId: reunionId,
      minuta: this.promiseWaitingFor(this.minutaService().getMinutaDeReunion(reunionId))
        .whenInterruptedAndReauthenticated(() => {
          this.navigator().navigateToAsistentesMinuta(reunionId);
        }),
      usuarios: this.promiseWaitingFor(this.userService().getAllUsers())
        .whenInterruptedAndReauthenticated(() => {
          this.navigator().navigateToUsers();
        }),
      votantes: this.promiseWaitingFor(this.userService().getVotantes(reunionId))
        .whenInterruptedAndReauthenticated(()=> {
          this.navigator().navigateToUsers();
        }),
    }).then((model)=> {
      return model;
    });
  }
});

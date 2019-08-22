import Ember from "ember";
import AuthenticatedRoute from "ateam-ember-authenticator/mixins/authenticated-route";
import NavigatorInjected from "../mixins/navigator-injected";
import ReunionServiceInjected from "../mixins/reunion-service-injected";

export default Ember.Route.extend(AuthenticatedRoute, ReunionServiceInjected, NavigatorInjected, {
  model(params) {
    this.promiseWaitingFor(this.reunionService().getProximaReunion())
      .whenInterruptedAndReauthenticated(()=> {
        this.navigator().navigateToProximaRoots();
      })
      .then((reunion)=> {
        this.navigator().navigateToReunionesEdit(reunion.get('id'), {temaAReproponer: params.id});
      });
  },

});

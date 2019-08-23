import Ember from "ember";
import AuthenticatedRoute from "ateam-ember-authenticator/mixins/authenticated-route";
import NavigatorInjected from "../../mixins/navigator-injected";
import ReunionServiceInjected from "../../mixins/reunion-service-injected";
import UserServiceInjected from "../../mixins/user-service-injected";
import {temasConComportamiento} from "../../concepts/tema";

export default Ember.Route.extend(AuthenticatedRoute,UserServiceInjected, ReunionServiceInjected, NavigatorInjected, {
  model() {
    return this.promiseWaitingFor(this.reunionService().getAllReuniones())
      .whenInterruptedAndReauthenticated(()=> {
        this.navigator().navigateToReuniones();
      })
      .then((reuniones)=> {
        reuniones.forEach((reunion)=> {
          reunion.set('temasPropuestos', temasConComportamiento(reunion.get('temasPropuestos'), null))
        });
        return reuniones;
      });
  },

});

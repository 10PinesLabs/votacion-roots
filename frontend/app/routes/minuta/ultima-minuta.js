import Ember from "ember";
import NavigatorInjected from "../../mixins/navigator-injected";
import MinutaServiceInjected from "../../mixins/minuta-service-injected";

export default Ember.Route.extend(NavigatorInjected, MinutaServiceInjected, {
  beforeModel: function () {
    this.minutaService().getUltimaMinuta()
      .then((minuta) => this.navigator().navigateToVerMinuta(minuta.reunionId),
        (error) => console.error(error));
  }
});

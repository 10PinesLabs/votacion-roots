import Ember from "ember";

export default Ember.Route.extend({
  actions: {
    error(transition, error) {
      debugger;
      alert(error);
    }
  },
});

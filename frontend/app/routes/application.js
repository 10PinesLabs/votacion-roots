import Ember from "ember";

export default Ember.Route.extend({
  actions: {
    error(transition, error) {
      alert(error);
    }
  },
});

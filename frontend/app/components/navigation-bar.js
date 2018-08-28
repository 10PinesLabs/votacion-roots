import Ember from "ember";
import AuthenticatorInjected from "ateam-ember-authenticator/mixins/authenticator-injected";
import Application from "../concepts/application";

export default Ember.Component.extend(AuthenticatorInjected, {
  tagName: 'nav',
  classNames: ['light-grey '],
  application: Application.create(),
  actions: {
    logout: function () {
      this.authenticator().logout();
    }
  }
});

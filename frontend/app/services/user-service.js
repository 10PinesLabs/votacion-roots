import Ember from "ember";
import EmberizedResourceCreatorInjected from "ateam-ember-resource/mixins/emberized-resource-creator-injected";
/**
 * Esta clase permite interactuar con el backend para modificar los usuarios
 */
export default Ember.Service.extend(EmberizedResourceCreatorInjected, {

  getCurrentUser(){
    return this._currentUserResource().getAll();
  },

  getAllUsers: function () {
    return this._userResource().getAll();
  },
  createUser: function () {
    return this._userResource().create();
  },
  getUser: function (userId) {
    return this._userResource().getSingle(userId);
  },
  updateUser: function (user) {
    return this._userResource().update(user);
  },
  removeUser: function (user) {
    return this._userResource().remove(user);
  },
  getNoVotantes: function (reunion) {
    return this._noVotaronUserResource().getSingle(reunion);
  },
  getVotantes: function (reunion){
    return this._votaronUserResource().getSingle(reunion);
  },
  // PRIVATE
  _userResource: function () {
    var resourceCreator = this.resourceCreator();
    var resource = resourceCreator.createResource('users');
    return resource;
  },
  _currentUserResource: function () {
    var resourceCreator = this.resourceCreator();
    var resource = resourceCreator.createResource('users/current');
    return resource;
  },
  _noVotaronUserResource: function () {
    var resourceCreator = this.resourceCreator();
    var resource = resourceCreator.createResource('users/noVotaron');
    return resource;
  },
  _votaronUserResource: function() {
    var resourceCreator = this.resourceCreator();
    var resource = resourceCreator.createResource('users/votaron');
    return resource;
  }

});

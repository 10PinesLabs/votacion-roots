import Ember from 'ember';
import EmberizedResourceCreatorInjected from "ateam-ember-resource/mixins/emberized-resource-creator-injected";
import {promiseHandling} from "../helpers/promise-handling";

export default Ember.Service.extend(EmberizedResourceCreatorInjected,{

  getMinutaDeReunion(reunion){
    return this._minutaDeReunionResource().getSingle(reunion);
  },

  updateMinuta(minuta){
    return promiseHandling(this._minutaResource().update(minuta));
  },

  //private
  _minutaResource: function () {
    var resourceCreator = this.resourceCreator();
    var resource = resourceCreator.createResource('minuta');
    return resource;
  },

  _minutaDeReunionResource: function () {
    var resourceCreator = this.resourceCreator();
    var resource = resourceCreator.createResource('minuta/reunion');
    return resource;
  }
});

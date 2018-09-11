/**
 * Created by fede on 22/06/17.
 */
import Ember from "ember";
import EmberizedResourceCreatorInjected from "ateam-ember-resource/mixins/emberized-resource-creator-injected";
import {promiseHandling} from "../helpers/promise-handling";
/**
 * Esta clase permite interactuar con el backend para modificar las duraciones
 */
export default Ember.Service.extend(EmberizedResourceCreatorInjected, {


  getAll:function(){
    return  this._duracionesResource().getAll();
  },

  // PRIVATE
  _duracionesResource: function () {
    var resourceCreator = this.resourceCreator();
    var resource = resourceCreator.createResource('duraciones');
    return resource;
  },

});

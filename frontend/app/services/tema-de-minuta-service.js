import Ember from 'ember';
import EmberizedResourceCreatorInjected from "ateam-ember-resource/mixins/emberized-resource-creator-injected";
import {promiseHandling} from "../helpers/promise-handling";

export default Ember.Service.extend(EmberizedResourceCreatorInjected,{

  getTemaDeMinuta(temaDeMinuta){
    return this._temaDeMinutaService().getSingle(temaDeMinuta);
  },
  updateTemaDeMinuta(temaDeMinuta){
    return this._temaDeMinutaService().update(temaDeMinuta);
  },
  //private
  _temaDeMinutaService: function () {
    var resourceCreator = this.resourceCreator();
    var resource = resourceCreator.createResource('temaDeMinuta');
    return resource;
  }
});

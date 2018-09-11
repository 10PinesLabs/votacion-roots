import Ember from "ember";
import EmberizedResourceCreatorInjected from "ateam-ember-resource/mixins/emberized-resource-creator-injected";
import {promiseHandling} from "../helpers/promise-handling";
/**
 * Esta clase permite interactuar con el backend para modificar las reuniones
 */
export default Ember.Service.extend(EmberizedResourceCreatorInjected, {

  getAllTemas: function () {
    return this._temaResource().getAll();
  },
  createTema: function (tema) {
    return promiseHandling(this._temaResource().create(tema));
  },
  getTema: function (userId) {
    return this._temaResource().getSingle(userId);
  },
  updateTema: function (tema) {
    return promiseHandling(this._temaResource().update(tema));
  },
  removeTema: function (tema) {
    return promiseHandling(this._temaResource().remove(tema));
  },
  votarTema:function(temaId){
    return promiseHandling(this._temaAgregarVotoResource().getSingle(temaId));
  },
  quitarVotoTema:function(temaId){
    return promiseHandling(this._temaQuitarVotosResource().getSingle(temaId));
  },


  // PRIVATE
  _temaResource: function () {
    return this._createResource('temas');
  },
  _temaAgregarVotoResource: function () {
    return this._createResource('temas/votar');
  },
  _temaQuitarVotosResource: function () {
    return this._createResource('temas/desvotar');
  },

  _createResource: function (source) {
    var resourceCreator = this.resourceCreator();
    var resource = resourceCreator.createResource(source);
    return resource;
  }

});

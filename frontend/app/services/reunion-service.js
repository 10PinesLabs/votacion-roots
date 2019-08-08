import Ember from "ember";
import EmberizedResourceCreatorInjected from "ateam-ember-resource/mixins/emberized-resource-creator-injected";
import {promiseHandling} from "../helpers/promise-handling";
/**
 * Esta clase permite interactuar con el backend para modificar las reuniones
 */
export default Ember.Service.extend(EmberizedResourceCreatorInjected, {

  getProximaReunion(){
    return this._proximaReunionResource().getAll();
  },
  cerrarReunion(reunion){
    var idDeReunion = reunion.get('id');
    return promiseHandling(this._cerrarReunionResource().getSingle(idDeReunion));
  },
  reabrirReunion(reunion){
    var idDeReunion = reunion.get('id');
    return promiseHandling(this._reabrirReunionResource().getSingle(idDeReunion));
  },

  getAllReuniones() {
    return this._reunionResource().getAll();
  },
  createReunion(proyecto) {
    return promiseHandling(this._reunionResource().create(proyecto));
  },
  getReunion(userId) {
    return this._reunionResource().getSingle(userId);
  },
  updateReunion(proyecto) {
    return promiseHandling(this._reunionResource().update(proyecto));
  },
  removeReunion(user) {
    return promiseHandling(this._reunionResource().remove(user));
  },
  getMinuta(reunion){
    return this._minutaReunionResource().getSingle(reunion);
  },
  proponerPino(reunion, pino){
    return promiseHandling(this._proponerPinoEnReunionResource(reunion.id).create({pino: pino}))
  },

  // PRIVATE
  _reunionResource: function () {
    var resourceCreator = this.resourceCreator();
    var resource = resourceCreator.createResource('reuniones');
    return resource;
  },
  _proximaReunionResource: function () {
    var resourceCreator = this.resourceCreator();
    var resource = resourceCreator.createResource('reuniones/proxima');
    return resource;
  },
  _cerrarReunionResource: function () {
    var resourceCreator = this.resourceCreator();
    var resource = resourceCreator.createResource('reuniones/cerrar');
    return resource;
  },
  _minutaReunionResource: function () {
    var resourceCreator = this.resourceCreator();
    var resource = resourceCreator.createResource('reuniones/minuta');
    return resource;
  },
  _reabrirReunionResource: function () {
    var resourceCreator = this.resourceCreator();
    var resource = resourceCreator.createResource('reuniones/reabrir');
    return resource;
  },
  _proponerPinoEnReunionResource: function (reunionId) {
    var resourceCreator = this.resourceCreator();
    var resource = resourceCreator.createResource('reuniones/' + reunionId + '/propuestas');
    return resource;
  },
});

import Ember from "ember";
import config from "./config/environment";

const Router = Ember.Router.extend({
  location: config.locationType,
  rootURL: config.rootURL
});

Router.map(function () {
  this.route('login');
  this.route('engaging-session');

  this.route('proxima-roots');
  this.route('reuniones', function () {
    this.route('edit', {path: "reuniones/:reunion_id"});
    this.route('list');
  });
  this.route('minuta', function(){
    this.route('editar', {path:":reunion_id/editar"});
    this.route('ver', {path:":reunion_id/ver"});
    this.route('ver-txt', {path:":reunion_id/ver-txt"});
  });

  this.route('users', function () {
    this.route('edit', {path: "edit/:user_id"});
  });

  this.route('temas-generales');

  // Catches all the malformed urls (not matching previous routes)
  this.route('wrong-paths', {path: '/*badUrl'});
  this.route('ver-minuta');
});

export default Router;

import Ember from "ember";
import TransitionerInjected from "ateam-ember-supplement/mixins/transitioner-injected";

/**
 * This type represents the application navigator that knows how to navigate to different sections of the applications
 * requiring the needed arguments in each case.
 *   This class abstracts ember routes and adds semantic specific to this app
 */
export default Ember.Service.extend(TransitionerInjected, {

  navigateToEngageSession(){
    this._navigateTo('engaging-session');
  },
  navigateToLogin(){
    this._navigateTo('login');
  },
  navigateToIndex(){
    this._navigateTo('index');
  },

  navigateToProyectos(){
    this._navigateTo('proyectos');
  },

  navigateToProximaRoots(){
    this._navigateTo('proxima-roots');
  },
  navigateToReuniones(){
    this._navigateTo('reuniones.list');
  },
  navigateToReunionesEdit(reunion, params){
    this._navigateTo('reuniones.edit', reunion, params);
  },
  navigateToAsistentesMinuta(reunion){
    this._navigateTo('minuta.asistentes', reunion);
  },

  navigateToConclusiones(reunionId){
    this._navigateTo('minuta.conclusiones', reunionId);
  },

  navigateToUsers(){
    this._navigateTo('users');
  },
  navigateToUsersEdit(user){
    this._navigateTo('users.edit', user);
  },

  navigateToTemasGenerales(){
    this._navigateTo('temas-generales');
  },

  navigateToVerMinuta(reunionId){
    this._navigateTo('minuta.ver', reunionId);
  },

  // PRIVATE
  _navigateTo(routeName, models, queryParams){
    this.transitioner().transitionTo(routeName, models, queryParams);
  }
});

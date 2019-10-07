import Ember from "ember";
import ReunionServiceInjected from "../../mixins/reunion-service-injected";
import NavigatorInjected from "../../mixins/navigator-injected";
import DuracionesServiceInjected from "../../mixins/duraciones-service-injected";
import UserServiceInjected from "../../mixins/user-service-injected";
import MinutaServiceInjected from "../../mixins/minuta-service-injected";
export default Ember.Controller.extend(ReunionServiceInjected,MinutaServiceInjected,UserServiceInjected, NavigatorInjected,DuracionesServiceInjected, {

  anchoDeTabla: 's12',

  reunionSeleccionada: Ember.computed('model.[]', 'indiceSeleccionado', function () {
    var indiceSeleccionado = this.get('indiceSeleccionado') || 0;
    var reuniones = this._reuniones();
    return reuniones.objectAt(indiceSeleccionado);
  }),

  reunionCerrada:Ember.computed('reunionSeleccionada',function(){
    var cerrada=(this.get('reunionSeleccionada.status')==="CERRADA") || (this.get('reunionSeleccionada.status')==="CON_MINUTA");

    if(cerrada){

       this.set('duracionDeReunion',180);
     }
     else{
       this.set('duracionDeReunion',0);
     }

    return cerrada;
  }),
  reunionMinuteada:Ember.computed('reunionSeleccionada','minuta',function(){

    return this.get('reunionSeleccionada.status') !=='CON_MINUTA';
}),
  temasEstimados: Ember.computed('duracionDeReunion',function(){

    var temas= this.get('reunionSeleccionada.temasPropuestos');
    var duracionRestante=this.duracionDeReunion;
    var i=0;
    var temasQueEntran=[];
    while(i<temas.length && duracionRestante>=this._obtenerDuracionDeTema(temas.get(i)).cantidadDeMinutos){
      temasQueEntran.push(temas.get(i));
      duracionRestante=duracionRestante- this._obtenerDuracionDeTema(temas.get(i)).cantidadDeMinutos;
      i++;
    }
    return temasQueEntran;
  }),

  ultimoTemaQueEntra: Ember.computed('temasEstimados,reunionSeleccionada',function(){

    var temasEstimados=this.get('temasEstimados');
    return temasEstimados[temasEstimados.length-1];
  }),

  actions: {

    verReunion(reunion){
      this._traerDuraciones().then(() => {
        this._mostrarDetalleDe(reunion);
      });
    },

    verNoVotantes(reunion){
      this._traerNoVotantes(reunion).then(() => {
        this._mostrarNoVotantes();
      });
    },

    cerrarDetalle(){
      this._ocultarDetalle();
    },

    cerrarMinuta(){
      this._ocultarMinuta();
    },

    cerrarNoVotantes(){
      this._ocultarNoVotantes();
    },

    // verMinuta(){
    //   // this._traerMinuta().then(()=>{
    //   //   this._mostrarMinuta();
    //   // });
    //   this.navigator().navigateToVerMinuta(reunion.get('id'));
    // },

    editarReunion(reunion){
      this.navigator().navigateToReunionesEdit(reunion.get('id'));
    },

    crearReunion(){
      this._guardarNuevaYRecargar();
    },

    borrarReunion(reunion){
      this.reunionService().removeReunion(reunion)
        .then(()=> {
          this._ocultarDetalle();
          this._recargarLista();
        });
    }
  },

  _mostrarDetalleDe(reunion){
    var indiceClickeado = this._reuniones().indexOf(reunion);
    this.set('indiceSeleccionado', indiceClickeado);
  },

  _guardarNuevaYRecargar(){
    this.reunionService().createReunion(Ember.Object.create())
      .then(()=> {
        this._recargarLista();
      });
  },

  _recargarLista(){
    this.get('target.router').refresh();
  },

  _ocultarMinuta(){
    this.set('mostrandoMinuta',false);
  },

  _ocultarNoVotantes(){
    this.set('mostrandoNoVotantes',false);
  },

  _reuniones(){
    return this.get('model');
  },

  _traerDuraciones(){
    return this.duracionesService().getAll().then((duraciones)=> {
      this.set('duraciones',duraciones);
    });
  },

  _obtenerDuracionDeTema(unTema){
   var duraciones= this.get('duraciones');
    return duraciones.find(function (duracion) {
     return duracion.nombre===unTema.duracion;
   });
  },

  _mostrarNoVotantes(){
    this.set('mostrandoNoVotantes',true);
  },

  _traerNoVotantes(reunion){
   return this.userService().getNoVotantes(reunion.id).then((noVotantes)=> {
     this.set('noVotantes', noVotantes);
   });
  }
});

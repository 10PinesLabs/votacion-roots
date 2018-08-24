import Ember from 'ember';

export default Ember.Component.extend({

  fueTratado: Ember.computed( 'temaDeMinuta.conclusion', function() {
    return !!(this.get('temaDeMinuta.conclusion'));
  }),
  guardarConclusionHabilitado:Ember.computed('temaDeMinuta.actionItems.@each.descripcion',
    'temaDeMinuta.actionItems.@each.responsables',
    'temaDeMinuta.actionItems.[]',function(){
    if(this.get('temaDeMinuta.actionItems').some((actionItem)=>{
        return  !actionItem.descripcion || actionItem.responsables.length<=0;
      })){
      return "disabled";
    }
    else{
      return "";
    }
  }),
  actions:{
    agregarActionItem(){
      this.get('temaDeMinuta').actionItems.pushObject(
        Ember.Object.extend().create({
          descripcion:"",
          responsables:[],
        }));
      this.rerender();
    },

    metodo(){
      var x = document.getElementById("toast");
      x.className = "show";
      setTimeout(function(){ x.className = x.className.replace("show", ""); }, 5000);
    },
    },
  });

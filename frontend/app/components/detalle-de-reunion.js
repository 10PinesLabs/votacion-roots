import Ember from 'ember';

export default Ember.Component.extend({

  tableTitles: Ember.computed('reunionCerrada', function(){
    const titulosDefault = '#, Título, Autor, Duración, Votos';
    const titulosConTemasTratados = titulosDefault + ', Tratado';
    return this.get('reunionCerrada') ? titulosConTemasTratados : titulosDefault;
  })

});

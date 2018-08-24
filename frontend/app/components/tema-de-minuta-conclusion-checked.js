import Ember from 'ember';

export default Ember.Component.extend({
    conclusionPresente: Ember.computed('temaDeMinuta', function () {
       if(this.get('temaDeMinuta.conclusion')){
        return 'done';
      }else{
         return '';
       }
    }),
});

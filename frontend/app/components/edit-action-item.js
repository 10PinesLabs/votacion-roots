import Ember from 'ember';

export default Ember.Component.extend({

  actions:{
    borrarActionItem(unActionItem){
      var actionItems= this.get('actionItems');
      actionItems.removeObject(unActionItem);
      this.rerender();
    },
  },
});

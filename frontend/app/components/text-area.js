import Ember from 'ember';

export default Ember.Component.extend({

  //Se hace esto para evitar que value quede en undefined, lo que genera un \n en el text area
  willRender() {
    this._super(...arguments);
    let value = this.get('value');
    if (value === undefined) {
      this.set('value', "");
    }
  }

});

import Ember from 'ember';

export default Ember.Component.extend({
  didInsertElement() {
    Ember.run.schedule('afterRender', this, this.measureDescriptionAndSet);
  },
  measureDescriptionAndSet(){
    this.set(
      "descriptionMaxHeight",
      Ember.String.htmlSafe("max-height: " +
        this.get("element").getElementsByClassName("descripcion-minuta")[0].scrollHeight +
        "px")
    );
  }
});

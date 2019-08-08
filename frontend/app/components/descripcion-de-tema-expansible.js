import Ember from 'ember';

export default Ember.Component.extend({
  esTemaDeProponerPino: Ember.computed('tema', function(){
    return this.get('tema.tipo') === "proponerPinos";
  }),
  didInsertElement() {
    Ember.run.scheduleOnce('afterRender', this, this.measureDescriptionAndSet);
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

import Ember from 'ember';

export default Ember.Component.extend({
  tagName: 'li',
  classNames: ['tab'],

  marcable: Ember.computed('route', function () {
    if (window.location.pathname.replace(/\d/g, "") === this.route) {
      return 'bold';
    }
  })
});

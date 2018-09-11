/* jshint expr:true */
import { expect } from 'chai';
import {
  describeComponent,
  it
} from 'ember-mocha';
import hbs from 'htmlbars-inline-precompile';

describeComponent(
  'each-card-tema',
  'Integration: EachCardTemaComponent',
  {
    integration: true
  },
  function() {
    it('renders', function() {
      // Set any properties with this.set('myProperty', 'value');
      // Handle any actions with this.on('myAction', function(val) { ... });
      // Template block usage:
      // this.render(hbs`
      //   {{#each-card-tema}}
      //     template content
      //   {{/each-card-tema}}
      // `);

      this.render(hbs`{{each-card-tema}}`);
      expect(this.$()).to.have.length(1);
    });
  }
);

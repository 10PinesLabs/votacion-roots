/* jshint expr:true */
import { expect } from 'chai';
import {
  describeComponent,
  it
} from 'ember-mocha';
import hbs from 'htmlbars-inline-precompile';

describeComponent(
  'vot-star',
  'Integration: VotStarComponent',
  {
    integration: true
  },
  function() {
    it('renders', function() {
      // Set any properties with this.set('myProperty', 'value');
      // Handle any actions with this.on('myAction', function(val) { ... });
      // Template block usage:
      // this.render(hbs`
      //   {{#vot-star}}
      //     template content
      //   {{/vot-star}}
      // `);

      this.render(hbs`{{vot-star}}`);
      expect(this.$()).to.have.length(1);
    });
  }
);

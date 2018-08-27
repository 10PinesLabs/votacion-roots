/* jshint expr:true */
import { expect } from 'chai';
import {
  describeComponent,
  it
} from 'ember-mocha';
import hbs from 'htmlbars-inline-precompile';

describeComponent(
  'tema-star',
  'Integration: TemaStarComponent',
  {
    integration: true
  },
  function() {
    it('renders', function() {
      // Set any properties with this.set('myProperty', 'value');
      // Handle any actions with this.on('myAction', function(val) { ... });
      // Template block usage:
      // this.render(hbs`
      //   {{#tema-star}}
      //     template content
      //   {{/tema-star}}
      // `);

      this.render(hbs`{{tema-star}}`);
      expect(this.$()).to.have.length(1);
    });
  }
);

/* jshint expr:true */
import { expect } from 'chai';
import {
  describeComponent,
  it
} from 'ember-mocha';
import hbs from 'htmlbars-inline-precompile';

describeComponent(
  'opcion-header',
  'Integration: OpcionHeaderComponent',
  {
    integration: true
  },
  function() {
    it('renders', function() {
      // Set any properties with this.set('myProperty', 'value');
      // Handle any actions with this.on('myAction', function(val) { ... });
      // Template block usage:
      // this.render(hbs`
      //   {{#opcion-header}}
      //     template content
      //   {{/opcion-header}}
      // `);

      this.render(hbs`{{opcion-header}}`);
      expect(this.$()).to.have.length(1);
    });
  }
);

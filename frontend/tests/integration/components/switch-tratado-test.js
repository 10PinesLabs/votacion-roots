/* jshint expr:true */
import { expect } from 'chai';
import {
  describeComponent,
  it
} from 'ember-mocha';
import hbs from 'htmlbars-inline-precompile';

describeComponent(
  'switch-tratado',
  'Integration: SwitchTratadoComponent',
  {
    integration: true
  },
  function() {
    it('renders', function() {
      // Set any properties with this.set('myProperty', 'value');
      // Handle any actions with this.on('myAction', function(val) { ... });
      // Template block usage:
      // this.render(hbs`
      //   {{#switch-tratado}}
      //     template content
      //   {{/switch-tratado}}
      // `);

      this.render(hbs`{{switch-tratado}}`);
      expect(this.$()).to.have.length(1);
    });
  }
);

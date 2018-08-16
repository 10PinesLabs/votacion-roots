/* jshint expr:true */
import { expect } from 'chai';
import {
  describeComponent,
  it
} from 'ember-mocha';
import hbs from 'htmlbars-inline-precompile';

describeComponent(
  'tema-de-minuta-en-tabla',
  'Integration: TemaDeMinutaEnTablaComponent',
  {
    integration: true
  },
  function() {
    it('renders', function() {
      // Set any properties with this.set('myProperty', 'value');
      // Handle any actions with this.on('myAction', function(val) { ... });
      // Template block usage:
      // tethis.render(hbs`
      //   {{#tema-de-minuta-en-tabla}}
      //     template content
      //   {{/tema-de-minuta-en-tabla}}
      // `);

      this.render(hbs`{{tema-de-minuta-en-tabla}}`);
      expect(this.$()).to.have.length(1);
    });
  }
);

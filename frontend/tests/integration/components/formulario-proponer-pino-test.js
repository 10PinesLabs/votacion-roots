/* jshint expr:true */
import { expect } from 'chai';
import {
  describeComponent,
  it
} from 'ember-mocha';
import hbs from 'htmlbars-inline-precompile';

describeComponent(
  'formulario-proponer-pino',
  'Integration: FormularioProponerPinoComponent',
  {
    integration: true
  },
  function() {
    it('renders', function() {
      // Set any properties with this.set('myProperty', 'value');
      // Handle any actions with this.on('myAction', function(val) { ... });
      // Template block usage:
      // this.render(hbs`
      //   {{#formulario-proponer-pino}}
      //     template content
      //   {{/formulario-proponer-pino}}
      // `);

      this.render(hbs`{{formulario-proponer-pino}}`);
      expect(this.$()).to.have.length(1);
    });
  }
);

/* jshint expr:true */
import { expect } from 'chai';
import {
  describeComponent,
  it
} from 'ember-mocha';
import hbs from 'htmlbars-inline-precompile';

describeComponent(
  'header-tema',
  'Integration: HeaderTemaComponent',
  {
    integration: true
  },
  function() {
    it('renders', function() {
      // Set any properties with this.set('myProperty', 'value');
      // Handle any actions with this.on('myAction', function(val) { ... });
      // Template block usage:
      // this.render(hbs`
      //   {{#header-tema}}
      //     template content
      //   {{/header-tema}}
      // `);

      this.render(hbs`{{header-tema}}`);
      expect(this.$()).to.have.length(1);
    });
  }
);

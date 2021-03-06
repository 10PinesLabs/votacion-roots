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
      this.render(hbs`{{tema-de-minuta-en-tabla}}`);
      expect(this.$()).to.have.length(1);
    });
  }
);

/* jshint expr:true */
import { expect } from 'chai';
import {
  describeComponent,
  it
} from 'ember-mocha';
import hbs from 'htmlbars-inline-precompile';

describeComponent(
  'modal-editor',
  'Integration: ModalEditorComponent',
  {
    integration: true
  },
  function() {
    it('renders', function() {
      // Set any properties with this.set('myProperty', 'value');
      // Handle any actions with this.on('myAction', function(val) { ... });
      // Template block usage:
      // this.render(hbs`
      //   {{#modal-editor}}
      //     template content
      //   {{/modal-editor}}
      // `);

      this.render(hbs`{{modal-editor}}`);
      expect(this.$()).to.have.length(1);
    });
  }
);

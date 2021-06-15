import Component from '@ember/component';
import { computed } from '@ember/object';

export default Component.extend({
  tagName: 'span',
  classNames: ['nowrap'],
  classNameBindings: ['responsiveLetterSpacing'],
  didInsertElement() {
    this._super(...arguments);
    this.set(
      'responsiveLetterSpacing',
      this.element.offsetWidth > this.boundary
    );
  },
});

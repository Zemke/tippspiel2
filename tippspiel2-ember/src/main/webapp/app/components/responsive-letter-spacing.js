import Component from '@ember/component';
import {computed} from '@ember/object';

export default Component.extend({
  tagName: 'span',
  classNames: ['nowrap'],
  classNameBindings: ['responsiveLetterSpacing'],
  didInsertElement() {
    this.set('responsiveLetterSpacing', this.get('element').offsetWidth > this.get('boundary'))
  }
});

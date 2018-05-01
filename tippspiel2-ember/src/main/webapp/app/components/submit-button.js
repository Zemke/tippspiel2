import {computed} from '@ember/object';
import Component from '@ember/component';

export default Component.extend({
  disabled: computed('model.validations.isInvalid', 'model.isSaving', function () {
    return this.get('model.validations.isInvalid') || this.get('model.isSaving')
  }),
  actions: {
    submit() {
      this.get('submit')();
    }
  }
});

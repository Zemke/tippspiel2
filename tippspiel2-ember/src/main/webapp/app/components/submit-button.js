import { computed } from '@ember/object';
import Component from '@ember/component';

export default Component.extend({
  disabled: computed.or('model.validations.isInvalid', 'model.isSaving'),
  actions: {
    submit() {
      this.submit();
    },
  },
});

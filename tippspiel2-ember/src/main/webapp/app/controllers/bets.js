import Controller from '@ember/controller';
import {computed} from '@ember/object';

export default Controller.extend({
  actions: {
    toggleShowPastFixtures() {
      this.toggleProperty('showPastFixtures');
    }
  },
  showPastFixtures: computed(function () {
    return false;
  })
});

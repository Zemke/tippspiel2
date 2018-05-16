import Component from '@ember/component';
import {computed} from '@ember/object';

export default Component.extend({
  evaluationIconClass: computed('evaluation', function () {
    const evaluation = this.get('evaluation');

    if (evaluation === 5) {
      return 'star';
    } else if (evaluation === 3) {
      return 'angle-double-up';
    } else if (evaluation === 1) {
      return 'angle-up';
    } else if (evaluation === 0) {
      return 'circle-o';
    }  else if (evaluation === 10) {
      return 'trophy';
    } else {
      return 'times';
    }
  })
});

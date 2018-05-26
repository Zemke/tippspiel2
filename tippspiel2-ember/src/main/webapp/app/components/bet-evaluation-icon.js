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
  }),
  tooltip: computed('evaluation', function () {
    const evaluation = this.get('evaluation');

    if (evaluation === 5) {
      return 'Exact result';
    } else if (evaluation === 3) {
      return 'Right goal difference';
    } else if (evaluation === 1) {
      return 'Right winner';
    } else if (evaluation === 0) {
      return 'Failed bet';
    }  else if (evaluation === 10) {
      return 'Right champion';
    } else {
      return 'No bet submitted';
    }
  })
});

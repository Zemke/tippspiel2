import Component from '@ember/component';
import { computed } from '@ember/object';

export default Component.extend({
  evaluationIconClass: computed('evaluation', function () {
    const evaluation = this.evaluation;

    if (evaluation === 5) {
      return 'star';
    } else if (evaluation === 3) {
      return 'angle-double-up';
    } else if (evaluation === 1) {
      return 'angle-up';
    } else if (evaluation === 0) {
      return 'dot-circle';
    } else if (evaluation === 10) {
      return 'trophy';
    } else {
      return 'times';
    }
  }),
  tooltip: computed('evaluation', function () {
    const evaluation = this.evaluation;

    if (evaluation === 5) {
      return '5';
    } else if (evaluation === 3) {
      return '3';
    } else if (evaluation === 1) {
      return '1';
    } else if (evaluation === 0) {
      return '0';
    } else if (evaluation === 10) {
      return '10';
    } else {
      return 'void';
    }
  }),
});

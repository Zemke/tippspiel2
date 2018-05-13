import Component from '@ember/component';
import {inject} from '@ember/service';
import {computed} from '@ember/object';

export default Component.extend({
  bet: inject('bet'),
  evaluation: computed(function () {
    return this.get('bet').evaluate(
      this.get('goalsHomeTeam'), this.get('goalsAwayTeam'),
      this.get('goalsHomeTeamBet'), this.get('goalsAwayTeamBet'));
  }),
  evaluationIconClass: computed('evaluation', function () {
    const evaluation = this.get('evaluation');

    if (evaluation === 5) {
      return 'star';
    } else if (evaluation === 3) {
      return 'angle-double-up';
    } else if (evaluation === 1) {
      return 'angle-up';
    } else if (evaluation === 0) {
      return 'circle';
    } else {
      return 'times';
    }
  })
});

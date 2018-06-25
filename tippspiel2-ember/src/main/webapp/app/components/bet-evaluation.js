import Component from '@ember/component';
import {inject} from '@ember/service';
import {computed} from '@ember/object';

export default Component.extend({
  bet: inject('bet'),
  evaluation: computed('goalsHomeTeam', 'goalsAwayTeam', function () {
    return this.get('bet').evaluate(
      this.get('goalsHomeTeam'), this.get('goalsAwayTeam'),
      this.get('goalsHomeTeamBet'), this.get('goalsAwayTeamBet'));
  })
});

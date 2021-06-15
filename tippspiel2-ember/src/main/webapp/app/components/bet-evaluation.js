import Component from '@ember/component';
import { inject } from '@ember/service';
import { computed } from '@ember/object';

export default Component.extend({
  bet: inject('bet'),
  evaluation: computed(
    'goalsAwayTeam',
    'goalsAwayTeamBet',
    'goalsHomeTeam',
    'goalsHomeTeamBet',
    function () {
      return this.bet.evaluate(
        this.goalsHomeTeam,
        this.goalsAwayTeam,
        this.goalsHomeTeamBet,
        this.goalsAwayTeamBet
      );
    }
  ),
});

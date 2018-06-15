import Component from '@ember/component';
import {computed} from '@ember/object';
import {inject} from '@ember/service';

export default Component.extend({
  classNames: ['columns', 'is-multiline'],
  bet: inject('bet'),
  didRender() {
    const evaluate = (betId) => {
      const bet = this.get('bets').findBy('id', betId);
      return this.get('bet').evaluate(
        bet.get('fixture.goalsHomeTeam'), bet.get('fixture.goalsAwayTeam'),
        bet.get('goalsHomeTeamBet'), bet.get('goalsAwayTeamBet'));
    };

    new Shuffle(this.get('element'))
      .sort({by: elem => evaluate(elem.getAttribute('data-bet-id')), reverse: true});

    return true;
  }
});

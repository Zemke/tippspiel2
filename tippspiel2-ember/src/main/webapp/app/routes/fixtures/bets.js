import Route from '@ember/routing/route';
import RSVP from 'rsvp';
import {inject} from '@ember/service';

export default Route.extend({
  bettingGame: inject(),
  bet: inject(),
  model(params) {
    return this.get('bettingGame.currentBettingGame').then(currentBettingGame => {
      return RSVP.hash({
        fixture: this.get('store').findRecord('fixture', params.fixture_id),
        bets: this.get('store').query('bet', {fixture: params.fixture_id, bettingGame: currentBettingGame.get('id')}),
        users: this.get('store').query('user', {bettingGame: currentBettingGame.get('id')})
      }).then(hash => {
        hash.bets = hash.fixture.get('date') - Date.now() > 0 ? [] : hash.users
          .map(u => hash.bets.find(b => b.get('user.id') === u.id) || this.get('store').createRecord('bet', {
            fixture: hash.fixture,
            user: u,
            bettingGame: this.get('store').peekRecord('betting-game', currentBettingGame.get('id'))
          }))
          .toArray()
          .map(b => {
            b._evaluation = this.get('bet').evaluate(
              b.get('fixture.goalsHomeTeam'), b.get('fixture.goalsAwayTeam'),
              b.get('goalsHomeTeamBet'), b.get('goalsAwayTeamBet'));
            if (b._evaluation === null) b._evaluation = -1;
            return b;
          })
          .sort((a, b) => b.get('_evaluation') - a.get('_evaluation'))
          .map(b => {
            delete b._evaluation;
            return b;
          });
        return hash;
      });
    });
  }
});

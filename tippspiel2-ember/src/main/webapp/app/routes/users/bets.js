import Route from '@ember/routing/route';
import RSVP from 'rsvp';
import { inject } from '@ember/service';
import NO_BETTING_STATES from '../../constants/no-betting-states';

export default Route.extend({
  bettingGame: inject(),
  model(params) {
    return this.get('bettingGame.currentBettingGame').then(
      (currentBettingGame) => {
        return RSVP.hash({
          user: this.store.findRecord('user', params.user_id),
          bets: this.store.query('bet', {
            user: params.user_id,
            bettingGame: currentBettingGame.get('id'),
          }),
          fixtures: this.store.query('fixture', {
            competition: currentBettingGame.get('competition.id'),
            status: NO_BETTING_STATES.join(','),
          }),
        }).then((hash) => {
          hash.bets = hash.fixtures
            .map(
              (f) =>
                hash.bets.find((b) => b.get('fixture.id') === f.id) ||
                this.store.createRecord('bet', {
                  fixture: f,
                  user: this.store.peekRecord('user', params.user_id),
                  bettingGame: this.store.peekRecord(
                    'betting-game',
                    currentBettingGame.get('id')
                  ),
                })
            )
            .toArray()
            .sort((a, b) => b.get('fixture.date') - a.get('fixture.date'));
          return hash;
        });
      }
    );
  },
});

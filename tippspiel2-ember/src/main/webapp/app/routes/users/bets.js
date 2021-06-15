import Route from '@ember/routing/route';
import RSVP from 'rsvp';
import { inject } from '@ember/service';

export default Route.extend({
  bettingGame: inject(),
  model(params) {
    return this.get('bettingGame.currentBettingGame').then(
      (currentBettingGame) => {
        return RSVP.hash({
          user: this.get('store').findRecord('user', params.user_id),
          bets: this.get('store').query('bet', {
            user: params.user_id,
            bettingGame: currentBettingGame.get('id'),
          }),
          fixtures: this.get('store').query('fixture', {
            competition: currentBettingGame.get('competition.id'),
            status: 'FINISHED,IN_PLAY',
          }),
        }).then((hash) => {
          hash.bets = hash.fixtures
            .map(
              (f) =>
                hash.bets.find((b) => b.get('fixture.id') === f.id) ||
                this.get('store').createRecord('bet', {
                  fixture: f,
                  user: this.get('store').peekRecord('user', params.user_id),
                  bettingGame: this.get('store').peekRecord(
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

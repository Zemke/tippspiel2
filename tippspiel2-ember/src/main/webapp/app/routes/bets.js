import Route from '@ember/routing/route';
import RSVP from 'rsvp';
import { inject } from '@ember/service';

export default Route.extend({
  auth: inject(),
  bettingGame: inject(),
  intl: inject(),
  model() {
    return RSVP.hash({
      user: this.get('auth.user'),
      currentBettingGame: this.get('bettingGame.currentBettingGame'),
    }).then((hash) =>
      RSVP.hash({
        fixtures: this.get('store').query('fixture', {
          competition: hash.currentBettingGame.get('competition.id'),
          complete: true,
        }),
        bets: this.get('store').query('bet', {
          user: hash.user.id,
          bettingGame: hash.currentBettingGame.get('id'),
        }),
      }).then((hash1) => {
        const bets = hash1.fixtures
          .map(
            (f) =>
              hash1.bets.find((b) => b.get('fixture.id') === f.id) ||
              this.get('store').createRecord('bet', {
                fixture: f,
                user: this.get('store').peekRecord('user', hash.user.id),
                bettingGame: this.get('store').peekRecord(
                  'betting-game',
                  hash.currentBettingGame.get('id')
                ),
              })
          )
          .toArray()
          .sort((a, b) => a.get('fixture.date') - b.get('fixture.date'));

        return {
          betsForFinishedFixtures: bets.filter(
            (b) => b.get('fixture.status') === 'FINISHED'
          ),
          betsForUnfinishedFixtures: bets.filter(
            (b) => b.get('fixture.status') !== 'FINISHED'
          ),
        };
      })
    );
  },
  actions: {
    willTransition(transition) {
      const hasUnsavedChanges = this.controller
        .get('model.betsForUnfinishedFixtures')
        .find(
          (bet) =>
            bet.get('hasDirtyAttributes') === true &&
            bet.get('validations.isValid')
        );

      if (
        hasUnsavedChanges != null &&
        !confirm('You have unsaved bets. Do you really want to leave?')
      ) {
        transition.abort();
      } else {
        return true;
      }
    },
  },
});

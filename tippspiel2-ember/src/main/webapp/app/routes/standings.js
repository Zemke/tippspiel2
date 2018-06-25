import Route from '@ember/routing/route';
import {inject} from '@ember/service';
import RSVP from 'rsvp';

export default Route.extend({
  bettingGame: inject(),
  auth: inject(),
  model() {
    return this.get('bettingGame.currentBettingGame').then(currentBettingGame =>
      RSVP.hash({
        standings: this.get('store').query('standing', {bettingGame: currentBettingGame.get('id')}),
        championBets: this.get('store').query('champion-bet', {bettingGame: currentBettingGame.get('id')}),
        authenticatedUser: this.get('auth.user'),
        competition: currentBettingGame.get('competition'),
      }).then(hash => {
        // TODO Rename liveFixtures to indicate it's actually bets and not fixtures.
        hash.liveFixtures = this.get('store').query('fixture', {status: 'IN_PLAY'}).then(liveFixtures =>
          this.get('store').query('bet', {
            fixture: liveFixtures.mapBy('id').join(','),
            user: hash.authenticatedUser.id
          }).then(betsForLiveFixtures =>
            liveFixtures.map(lF =>
              betsForLiveFixtures.find(b => b.get('fixture.id') === lF.get('id')) || this.get('store').createRecord('bet', {
                fixture: lF,
                user: this.get('store').peekRecord('user', hash.authenticatedUser.id),
                bettingGame: this.get('store').peekRecord('betting-game', currentBettingGame.get('id'))
              }))));

        return RSVP.hash(hash);
      })
    );
  }
});

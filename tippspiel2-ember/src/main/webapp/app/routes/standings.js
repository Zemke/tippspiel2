import Route from '@ember/routing/route';
import {inject} from '@ember/service';
import RSVP from 'rsvp';

export default Route.extend({
  bettingGame: inject(),
  model() {
    return this.get('bettingGame.currentBettingGame').then(currentBettingGame =>
      RSVP.hash({
        standings: this.get('store').query('standing', {bettingGame: currentBettingGame.get('id')}),
        championBets: this.get('store').query('champion-bet', {bettingGame: currentBettingGame.get('id')})
      })
    );
  }
});

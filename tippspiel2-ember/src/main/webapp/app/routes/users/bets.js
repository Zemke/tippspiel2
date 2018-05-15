import Route from '@ember/routing/route';
import RSVP from 'rsvp';
import {inject} from '@ember/service';

export default Route.extend({
  bettingGame: inject(),
  model(params) {
    return this.get('bettingGame.currentBettingGame').then(currentBettingGame => {
      return RSVP.hash({
        user: this.get('store').findRecord('user', params.user_id),
        bets: this.get('store').query('bet', {user: params.user_id, bettingGame: currentBettingGame.get('id')})
      });
    });
  }
});

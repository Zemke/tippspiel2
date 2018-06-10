import Route from '@ember/routing/route';
import RSVP from 'rsvp';
import {inject} from '@ember/service';

export default Route.extend({
  bettingGame: inject(),
  model(params) {
    return this.get('bettingGame.currentBettingGame').then(currentBettingGame => {
      return RSVP.hash({
        fixture: this.get('store').findRecord('fixture', params.fixture_id),
        bets: this.get('store').query('bet', {fixture: params.fixture_id, bettingGame: currentBettingGame.get('id')})
      });
    });
  }
});

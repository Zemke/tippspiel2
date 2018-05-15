import Route from '@ember/routing/route';
import {inject} from '@ember/service';

export default Route.extend({
  bettingGame: inject(),
  model() {
    return this.get('bettingGame.currentBettingGame').then(currentBettingGame =>
      this.get('store').query('standing', {bettingGame: currentBettingGame.get('id')}));
  }
});

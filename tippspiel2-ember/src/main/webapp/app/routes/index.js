import Route from '@ember/routing/route';
import { inject } from '@ember/service';

export default Route.extend({
  bettingGame: inject(),
  model() {
    return this.get('store').findAll('team');
  },
  redirect() {
    this.get('bettingGame.currentBettingGame')
      .then(() => this.transitionTo('standings'))
      .catch(() => {});
  },
});

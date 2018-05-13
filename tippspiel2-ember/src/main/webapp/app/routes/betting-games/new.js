import Route from '@ember/routing/route';
import RSVP from 'rsvp';
import {inject} from '@ember/service';

export default Route.extend({
  resHandler: inject(),
  model(params, transition) {
    return RSVP.hash({
      bettingGame: this.store.createRecord('betting-game'),
      communities: this.store.findAll('community'),
      currentCompetition: this.store.queryRecord('competition', {})
        .catch(err => {
          this.get('resHandler').handleError(err);
          transition.abort();
        })
    }).then(res => {
      res.bettingGame.set('competition', res.currentCompetition);
      return res;
    });
  }
});

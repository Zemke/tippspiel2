import Route from '@ember/routing/route';
import RSVP from 'rsvp';
import {inject} from '@ember/service';

export default Route.extend({
  bettingGame: inject(),
  model() {
    const competitions = this.get('store').findAll('competition');

    return RSVP.hash({
      competitions,
      currentCompetition: competitions.then(competitions => competitions.findBy('current', true))
    });
  }
});

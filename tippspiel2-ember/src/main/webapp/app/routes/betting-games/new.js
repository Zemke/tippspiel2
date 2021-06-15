import Route from '@ember/routing/route';
import RSVP from 'rsvp';
import { inject } from '@ember/service';

export default Route.extend({
  resHandler: inject(),
  auth: inject(),
  model(params, transition) {
    return RSVP.hash({
      bettingGame: this.store.createRecord('betting-game'),
      currentCompetition: this.store
        .query('competition', { current: true })
        .then((currentCompetitions) => currentCompetitions.get('firstObject'))
        .catch((err) => {
          this.resHandler.handleError(err);
          transition.abort();
        }),
    }).then((res) => {
      res.bettingGame.set('competition', res.currentCompetition);
      return res;
    });
  },
  beforeModel(transition) {
    return this.get('auth.isAdmin').then(
      (isAdmin) =>
        !isAdmin &&
        this.resHandler.handleWithRouting(
          transition,
          this.transitionTo.bind(this),
          'catchError.accessDenied'
        )
    );
  },
});

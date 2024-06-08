import Route from '@ember/routing/route';
import { inject } from '@ember/service';

export default Route.extend({
  auth: inject(),
  resHandler: inject(),
  model(model, transition) {
    return this.store
      .query('betting-game', { 'invitation-token': model.invitationToken })
      .then((bettingGames) => {
        if (!bettingGames.get('length')) {
          this.resHandler.handleWithRouting(
            transition,
            this.transitionTo.bind(this),
            'catchError.accessDenied'
          );
        }
        return this.store.createRecord('user', {
          bettingGames: [bettingGames.objectAt(0)],
        });
      });
  },
  beforeModel(transition) {
    if (
      transition.to.queryParams == null ||
      transition.to.queryParams['invitation-token'] == null
    ) {
      this.resHandler.handleWithRouting(
        transition,
        this.transitionTo.bind(this),
        'catchError.accessDenied'
      );
    }

    this.get('auth.user')
      .then(() =>
        this.transitionTo('join', { queryParams: transition.to.queryParams })
      )
      .catch(() => {});
  },
});

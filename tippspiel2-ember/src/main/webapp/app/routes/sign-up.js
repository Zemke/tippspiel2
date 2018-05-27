import Route from '@ember/routing/route';
import {inject} from "@ember/service"

export default Route.extend({
  resHandler: inject(),
  model(model, transition) {
    return this.get('store').query('betting-game', {'invitation-token': model.invitationToken})
      .then(bettingGames => {
        if (!bettingGames.get('length')) {
          this.get('resHandler').handleWithRouting(transition, this.transitionTo.bind(this), "catchError.accessDenied");
        }
        return this.store.createRecord('user', {bettingGames: [bettingGames.objectAt(0)]});
      });
  }
});

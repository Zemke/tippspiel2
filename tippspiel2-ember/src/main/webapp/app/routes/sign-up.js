import Route from '@ember/routing/route';
import {inject} from "@ember/service"
import $ from "jquery"

export default Route.extend({
  auth: inject(),
  resHandler: inject(),
  model(model, transition) {
    return this.get('store').query('betting-game', {'invitation-token': model.invitationToken})
      .then(bettingGames => {
        if (!bettingGames.get('length')) {
          this.get('resHandler').handleWithRouting(transition, this.transitionTo.bind(this), "catchError.accessDenied");
        }
        return this.store.createRecord('user', {bettingGames: [bettingGames.objectAt(0)]});
      });
  },
  beforeModel(transition) {
    if (transition.queryParams == null || transition.queryParams['invitation-token'] == null) {
      this.get('resHandler').handleWithRouting(transition, this.transitionTo.bind(this), "catchError.accessDenied")
    }

    this.get('auth.user')
      .then(() => this.transitionTo('join', {queryParams: transition.queryParams}))
      .catch($.noop);
  }
});

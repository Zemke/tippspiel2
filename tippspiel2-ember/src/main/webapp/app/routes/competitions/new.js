import Route from '@ember/routing/route';
import {inject} from '@ember/service';

export default Route.extend({
  auth: inject(),
  model() {
    return this.store.createRecord('competition');
  },
  beforeModel(transition) {
    return this.get('auth.isAdmin').then(isAdmin =>
      !isAdmin && this.get('resHandler').handleWithRouting(transition, this.transitionTo.bind(this), "Access denied"))
  }
});

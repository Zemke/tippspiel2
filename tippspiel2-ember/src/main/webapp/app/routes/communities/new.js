import Route from '@ember/routing/route';
import RSVP from 'rsvp';
import {computed} from '@ember/object';
import {inject} from '@ember/service';

export default Route.extend({
  auth: inject(),
  resHandler: inject(),
  model() {
    return RSVP.hash({
      community: this.store.createRecord('community'),
      users: this.store.findAll('user'),
    });
  },
  beforeModel(transition) {
    return this.get('auth.isAdmin').then(isAdmin =>
      !isAdmin && this.get('resHandler').handleWithRouting(transition, this.transitionTo.bind(this), "Access denied"))
  }
});

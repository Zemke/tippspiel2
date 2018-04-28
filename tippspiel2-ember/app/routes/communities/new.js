import Route from '@ember/routing/route';
import RSVP from 'rsvp';

export default Route.extend({
  model() {
    return RSVP.hash({
      community: this.store.createRecord('community'),
      users: this.store.findAll('user'),
    });
  }
});

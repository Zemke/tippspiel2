import Route from '@ember/routing/route';
import RSVP from 'rsvp';
import {computed} from '@ember/object';

export default Route.extend({
  model() {
    return RSVP.hash({
      community: this.store.createRecord('community'),
      users: this.store.findAll('user'),
      // usersFullNameInCommunity: computed('community.users.[]', () => {
      //   console.log('calling');
      //   return this.get('community').get('users').sortBy('lastName', 'firstName')
      // })
    });
  }
});

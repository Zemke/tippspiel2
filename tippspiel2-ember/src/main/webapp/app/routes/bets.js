import Route from '@ember/routing/route';
import RSVP from 'rsvp';
import {inject} from '@ember/service';

export default Route.extend({
  auth: inject(),
  model() {
    return this.get('auth.user').then(user =>
      RSVP.hash({
        fixtures: this.get('store').query('fixture', {competition: 467}),
        bets: this.get('store').query('bet', {user: user.id, /*bettingGame: 0*/}) // #GH-33
      }).then(res =>
        res.fixtures
          .map(f => res.bets.find(b => b.get('fixture.id') === f.id) || this.get('store').createRecord('bet', {
            fixture: f,
            user: this.get('store').peekRecord('user', user.id),
            // bettingGame: this.get('store').peekRecord('user', user.id) #GH-33
          }))
      ));
  }
});

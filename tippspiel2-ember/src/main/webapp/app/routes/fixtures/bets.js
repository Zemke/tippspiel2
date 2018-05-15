import Route from '@ember/routing/route';
import RSVP from 'rsvp';

export default Route.extend({
  model(params) {
    return RSVP.hash({
      fixture: this.get('store').findRecord('fixture', params.fixture_id),
      bets: this.get('store').query('bet', {fixture: params.fixture_id})
    });
  }
});

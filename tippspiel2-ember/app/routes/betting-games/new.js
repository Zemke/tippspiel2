import Route from '@ember/routing/route';
import RSVP from 'rsvp';

export default Route.extend({
  model(params, transition) {
    return RSVP.hash({
      bettingGame: this.store.createRecord('betting-game'),
      communities: this.store.findAll('community'),
      currentCompetition: this.store.queryRecord('competition', {})
        .catch(err => {
          if (err.status === 404) {
            iziToast.error({message: "There currently is no active competition."});
            transition.abort();
          }
        })
    }).then(res => {
      res.bettingGame.set('competition', res.currentCompetition);
      return res;
    });
  }
});

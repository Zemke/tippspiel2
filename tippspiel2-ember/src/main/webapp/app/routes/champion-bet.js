import Route from '@ember/routing/route';
import RSVP from 'rsvp';
import {inject} from '@ember/service';

export default Route.extend({
  auth: inject(),
  bettingGame: inject(),
  model() {
    return RSVP.hash({
      currentBettingGame: this.get('bettingGame.currentBettingGame'),
      authenticatedUser: this.get('auth.user')
    }).then(hash =>
      RSVP.hash({
        teams: this.get('store').findAll('team', {reload: true}).then(teams => teams.sortBy('name')), // TODO Sort by localized name.
        championBet: this.get('store').query(
          'champion-bet',
          {
            competition: hash.currentBettingGame.get('competition.id'),
            user: hash.authenticatedUser.id
          }
        ).then(championBets => {
          return !championBets.get('length')
            ? this.get('store').createRecord(
              'champion-bet',
              {
                user: this.get('store').peekRecord('user', hash.authenticatedUser.id),
                bettingGame: this.get('store').peekRecord('betting-game', hash.currentBettingGame.get('id')),
              }
            )
            : championBets.get('firstObject');
        })
      })
    );
  }
});

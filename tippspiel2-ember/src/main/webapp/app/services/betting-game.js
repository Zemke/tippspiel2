import DS from 'ember-data';
import {computed} from '@ember/object';
import Service, {inject} from '@ember/service';
import {Promise} from 'rsvp';

export default Service.extend({
  store: inject(),
  auth: inject(),
  currentBettingGame: computed(function () {
    return DS.PromiseObject.create({
      promise: new Promise((resolve, reject) => {
        this.get('auth.user')
          .then(authenticatedUser =>
            this.get('store').findRecord('user', authenticatedUser.id)
              .then(user => {
                const bettingGames = user.get('bettingGames').filter(bG => bG.get('competition.current') === true);
                if (!bettingGames.length) return reject({status: 401, message: 'Access denied.'});
                const bettingGameIdFromStorage = this.getRememberedCurrentBettingGame();
                const bettingGame =
                  (bettingGameIdFromStorage && bettingGames.findBy('id', bettingGameIdFromStorage))
                  || (bettingGames.objectAt(0));
                this.rememberCurrentBettingGame(bettingGame.get('id'));
                return resolve(bettingGame);
              }))
          .catch(() => reject({status: 401, message: 'Access denied.'}));
      })
    });
  }),
  rememberCurrentBettingGame(currentBettingGameId) {
    localStorage.setItem('betting-game', currentBettingGameId);
  },
  getRememberedCurrentBettingGame() {
    return localStorage.getItem('betting-game');
  }
});

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
            this.get('store').findAll('betting-game')
              .then(allBettingGames => {
                return this.bettingGamesWithUserAndCurrentCompetition(allBettingGames, authenticatedUser)
              })
              .then(filteredBettingGames => {
                if (!filteredBettingGames.length) return reject({status: 401});
                const bettingGameIdFromStorage = this.getCurrentBettingGame();
                const bettingGame =
                  (bettingGameIdFromStorage && filteredBettingGames.findBy('id', bettingGameIdFromStorage))
                  || (filteredBettingGames.objectAt(0));
                this.setCurrentBettingGame(bettingGame.get('id'));
                return resolve(bettingGame);
              }));
      })
    });
  }),
  setCurrentBettingGame(currentBettingGameId) {
    localStorage.setItem('betting-game', currentBettingGameId);
  },
  getCurrentBettingGame() {
    return localStorage.getItem('betting-game');
  },
  bettingGamesWithUserAndCurrentCompetition(bettingGames, user) {
    return bettingGames
      .filter(bG => bG.get('competition.current'))
      .filter(bG => bG.get('community.users')
        .mapBy('id')
        .includes(user.id.toString()));
  }
});

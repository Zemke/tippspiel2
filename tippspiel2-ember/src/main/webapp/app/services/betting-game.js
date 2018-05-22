import DS from 'ember-data';
import {computed} from '@ember/object';
import Service, {inject} from '@ember/service';

export default Service.extend({
  store: inject(),
  auth: inject(),
  currentBettingGame: computed(function () {
    return DS.PromiseObject.create({
      promise: this.get('auth.user')
        .then(authenticatedUser =>
          this.get('store').findAll('betting-game')
            .then(allBettingGames =>
              this.bettingGamesWithUser(allBettingGames, authenticatedUser))
            .then(bettingGamesWithUser => {
              if (!bettingGamesWithUser.length) return null;
              const bettingGameIdFromStorage = this.getCurrentBettingGame();
              const bettingGame =
                (bettingGameIdFromStorage && bettingGamesWithUser.findBy('id', bettingGameIdFromStorage))
                || (bettingGamesWithUser.objectAt(0));
              this.setCurrentBettingGame(bettingGame.get('id'));
              return bettingGame;
            }))
    });
  }),
  setCurrentBettingGame(currentBettingGameId) {
    localStorage.setItem('betting-game', currentBettingGameId);
  },
  getCurrentBettingGame() {
    return localStorage.getItem('betting-game');
  },
  bettingGamesWithUser(bettingGames, user) {
    return bettingGames
      .filter(bG => bG.get('community.users')
        .mapBy('id')
        .includes(user.id.toString()));
  }
});


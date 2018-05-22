import Component from '@ember/component';
import {computed} from '@ember/object';
import {inject} from '@ember/service';
import RSVP from "rsvp";
import DS from "ember-data";

export default Component.extend({
  auth: inject(),
  bettingGame: inject(),
  classNames: ['select', 'is-primary'],
  actions: {
    changeBettingGame(selectedBettingGameId) {
      this.get('bettingGame').setCurrentBettingGame(
        this.get('bettingGames').findBy('id', selectedBettingGameId).get('id'));
      location.reload();
    }
  },
  otherBettingGames: computed('bettingGames', function () {
    return DS.PromiseArray.create({
      promise: RSVP.hash({
        currentBettingGame: this.get('bettingGame.currentBettingGame'),
        bettingGames: this.get('bettingGames'),
        user: this.get('auth.user'),
      }).then((hash) =>
        this.get('bettingGame').bettingGamesWithUserAndCurrentCompetition(hash.bettingGames, hash.user)
          .filter(bG => bG.get('id') !== hash.currentBettingGame.get('id')))
    });
  })
});

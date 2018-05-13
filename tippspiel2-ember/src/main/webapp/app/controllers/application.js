import DS from 'ember-data';
import Controller from '@ember/controller';
import {computed} from '@ember/object';
import {inject} from '@ember/service';
import RSVP from 'rsvp';

export default Controller.extend({
  intl: inject(),
  auth: inject(),
  bettingGame: inject(),
  actions: {
    toggleLocale() {
      try {
        const newLocale = this.get('intl').get('locale')[0] === 'de' ? 'en-us' : 'de';
        this.get('intl').setLocale([newLocale]);
        localStorage.setItem('locale', newLocale);
        this.set('localeIsEnUs', newLocale === 'en-us');
      } catch (e) {
        this.get('intl').setLocale(['en-us']);
        localStorage.setItem('locale', 'en-us');
        this.set('localeIsEnUs', true);
      }
    },
    changeBettingGame(selectedBettingGameId) {
      this.get('bettingGame').setCurrentBettingGame(
        this.get('model.bettingGames').findBy('id', selectedBettingGameId).get('id'));
      location.reload();
    },
    signOut() {
      this.get('auth').wipeToken();
      window.location.href = '/'
    }
  },
  otherBettingGames: computed('bettingGame.currentBettingGame', 'model.bettingGames', function () {
    return DS.PromiseArray.create({
      promise: RSVP.hash({
        currentBettingGame: this.get('bettingGame.currentBettingGame'),
        bettingGames: this.get('model.bettingGames'),
        user: this.get('auth.user'),
      }).then((hash) =>
        this.get('bettingGame').bettingGamesWithUser(hash.bettingGames, hash.user)
          .filter(bG => bG.get('id') !== hash.currentBettingGame.get('id')))
    });
  }),
  isOnlyOneBettingGame: computed('otherBettingGames', function () {
    return DS.PromiseObject.create({promise: this.get('otherBettingGames').then(res => res.length === 0)});
  })
});

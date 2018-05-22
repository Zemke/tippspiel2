import DS from 'ember-data';
import Controller from '@ember/controller';
import {computed} from '@ember/object';
import {inject} from '@ember/service';

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
    signOut() {
      this.get('auth').wipeToken();
      window.location.href = '/'
    }
  },
  bettingGames: computed('bettingGame.currentBettingGame', 'auth.user', function () {
    const promise = this.get('auth.user').then(authenticatedUser =>
      this.get('bettingGame').bettingGamesWithUserAndCurrentCompetition(this.get('store').peekAll('betting-game'), authenticatedUser));
    return DS.PromiseArray.create({promise: promise})
  }),
  isOnlyOneBettingGame: computed('bettingGames', function () {
    const promise = this.get('bettingGames').then(bettingGames => bettingGames.get('length') === 1);
    return DS.PromiseObject.create({promise: promise})
  })
});

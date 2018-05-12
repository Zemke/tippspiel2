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
    changeBettingGame(selectedBettingGameId) {
      this.get('bettingGame').setCurrentBettingGame(
        this.get('model.bettingGames').findBy('id', selectedBettingGameId).get('id'));
      window.location.href = '/'
    },
    signOut() {
      this.get('auth').wipeToken();
      window.location.href = '/'
    }
  },
  otherBettingGames: computed('bettingGame.currentBettingGame', function () {
    return DS.PromiseArray.create({
      promise: this.get('bettingGame.currentBettingGame').then(currentBettingGame =>
        this.get('model.bettingGames').filter(bG => bG.get('id') !== currentBettingGame.get('id')))
    });
  })
});

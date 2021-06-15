import Controller from '@ember/controller';
import { computed } from '@ember/object';
import { inject } from '@ember/service';

export default Controller.extend({
  intl: inject(),
  auth: inject(),
  bettingGame: inject(),
  actions: {
    toggleLocale() {
      const newLocale =
        this.get('intl').get('locale')[0] === 'de' ? 'en-us' : 'de';
      this.get('intl').setLocale(newLocale);
      localStorage.setItem('locale', newLocale);
      this.set('localeIsEnUs', newLocale === 'en-us');
    },
    signOut() {
      this.get('auth').wipeToken();
      window.location.href = '/';
    },
    changeBettingGame(selectedBettingGame) {
      this.get('bettingGame').rememberCurrentBettingGame(
        selectedBettingGame.get('id')
      );
      location.reload();
    },
  },
  localeIsEnUs: computed(function () {
    const locale = this.get('intl').get('locale');
    return locale != null && locale[0] === 'en-us';
  }),
  isAppleStandalone: computed(function () {
    return 'standalone' in window.navigator && window.navigator.standalone;
  }),
});

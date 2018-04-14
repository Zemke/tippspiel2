import Controller from '@ember/controller';
import {computed} from '@ember/object';
import {inject} from '@ember/service';

export default Controller.extend({
  intl: inject(),
  auth: inject(),
  actions: {
    toggleLocale() {
      try {
        this.get('intl').setLocale([this.get('intl').get('locale')[0] === 'de' ? 'en-us' : 'de'])
      } catch (e) {
        this.get('intl').setLocale(['en-us'])
      }
      this.set('localeIsEnUs', this.get('intl').get('locale')[0] === 'en-us');
    },
    signOut() {
      this.get('auth').signOut();
      iziToast.success({message: "You’ve been signed out."});
    }
  },
});

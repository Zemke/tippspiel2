import Route from '@ember/routing/route';
import {inject} from '@ember/service';

export default Route.extend({
  actions: {
    toggleLocale() {
      try {
        this.get('intl').setLocale([this.get('intl').get('locale')[0] === 'de' ? 'en-us' : 'de'])
      } catch (e) {
        this.get('intl').setLocale(['en-us'])
      }
    }
  },
  intl: inject(),
  beforeModel() {
    return this.get('intl').setLocale(['en-us']);
  }
});

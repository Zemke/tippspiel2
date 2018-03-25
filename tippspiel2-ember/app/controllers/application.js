import Controller from '@ember/controller';
import {inject} from '@ember/service';

export default Controller.extend({
  intl: inject(),
  init() {
    this._super(...arguments);
    this.set('currentLocale', this.get('intl').get('locale')[0])
  },
  localeIsEnUs: function() {
    return this.get('currentLocale') === 'en-us';
  }.property('currentLocale'),
  actions: {
    toggleLocale() {
      try {
        this.get('intl').setLocale([this.get('intl').get('locale')[0] === 'de' ? 'en-us' : 'de'])
      } catch (e) {
        this.get('intl').setLocale(['en-us'])
      }
      this.set('currentLocale', this.get('intl').get('locale')[0]);
    }
  },
});

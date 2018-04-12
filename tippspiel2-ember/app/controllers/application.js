import Controller from '@ember/controller';
import {computed} from '@ember/object';
import {inject} from '@ember/service';

export default Controller.extend({
  intl: inject(),
  init() {
    this._super(...arguments);
    this.set('currentLocale', this.get('intl').get('locale')[0]);
    iziToast.settings({position: 'topRight'});
  },
  localeIsEnUs: computed('currentLocale', function () {
    return this.get('currentLocale') === 'en-us';
  }),
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

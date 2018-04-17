import Route from '@ember/routing/route';
import {computed} from '@ember/object';
import {inject} from '@ember/service';
import RSVP from 'rsvp';

export default Route.extend({
  intl: inject(),
  auth: inject(),
  model() {
    return RSVP.hash({
      localeIsEnUs: computed(() =>  {
        return this.get('intl').get('locale')[0] === 'en-us';
      }),
    })
  },
  beforeModel() {
    iziToast.settings({position: 'topRight'});

    if (this.get('auth').token != null) {
      this.get('store').findRecord('auth', this.get('auth').token)
        .then(res => this.get('auth').signIn(res.id))
        .catch(() => this.get('auth').signOut());
    }

    try {
      const locale = localStorage.getItem('locale') || 'en-us';
      this.get('intl').setLocale([locale]);
      localStorage.setItem('locale', locale);
    } catch (e) {
      this.get('intl').setLocale(['en-us']);
      localStorage.setItem('locale', 'en-us');
    }
  }
});

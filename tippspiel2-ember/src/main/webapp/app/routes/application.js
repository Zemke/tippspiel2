import Route from '@ember/routing/route';
import {computed} from '@ember/object';
import {inject} from '@ember/service';

export default Route.extend({
  intl: inject(),
  auth: inject(),
  resHandler: inject(),
  model() {
    return {
      localeIsEnUs: computed(() => this.get('intl').get('locale')[0] === 'en-us')
    }
  },
  beforeModel() {
    iziToast.settings({position: 'topRight'});

    try {
      const locale = localStorage.getItem('locale') || 'en-us';
      this.get('intl').setLocale([locale]);
      localStorage.setItem('locale', locale);
    } catch (e) {
      this.get('intl').setLocale(['en-us']);
      localStorage.setItem('locale', 'en-us');
    }
  },
  actions: {
    error(error, transition) {
      if (error.status === 404) {
        alert('Page not found.');
      } else if (error.status === 503) {
        alert('Service temporarily unavailable.');
      } else if (error.status === 401 || error.status === 403) {
        alert('Access denied.');
      } else {
        alert('An unknown error occurred.');
      }

      this.transitionTo('application')
    }
  }
});

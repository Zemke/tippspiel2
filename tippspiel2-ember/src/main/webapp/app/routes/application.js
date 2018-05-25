import Route from '@ember/routing/route';
import {computed} from '@ember/object';
import {inject} from '@ember/service';
import RSVP from 'rsvp';

export default Route.extend({
  intl: inject(),
  auth: inject(),
  resHandler: inject(),
  bettingGame: inject(),
  model() {
    return RSVP.hash({
      localeIsEnUs: computed(() => this.get('intl').get('locale')[0] === 'en-us'),
      currentBettingGame: this.get('bettingGame.currentBettingGame')
    });
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
      console.error(error);

      let message;
      if (error.status === 404) {
        message = 'Page not found.';
      } else if (error.status === 503) {
        message = 'Service temporarily unavailable.';
      } else if (error.status === 401 || error.status === 403) {
        message = 'Access denied.';
      } else {
        message = 'An unknown error occurred.';
      }

      this.get('resHandler').handleWithRouting(
        transition, this.transitionTo.bind(this), message);
    }
  }
});

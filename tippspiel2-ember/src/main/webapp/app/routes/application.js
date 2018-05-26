import Route from '@ember/routing/route';
import {computed} from '@ember/object';
import {inject} from '@ember/service';
import RSVP from 'rsvp';
import DS from "ember-data";

export default Route.extend({
  intl: inject(),
  auth: inject(),
  resHandler: inject(),
  bettingGame: inject(),
  model() {
    const currentBettingGame = this.get('bettingGame.currentBettingGame');
    const bettingGames = currentBettingGame.then(currentBettingGame => {
      const promise = this.get('auth.user').then(authenticatedUser => this.get('store').peekAll('betting-game'));
      return DS.PromiseArray.create({promise: promise})
    });
    return RSVP.hash({
      localeIsEnUs: computed(() => this.get('intl').get('locale')[0] === 'en-us'),
      currentBettingGame: currentBettingGame,
      bettingGames: bettingGames,
      isOnlyOneBettingGame: DS.PromiseObject.create({promise: bettingGames.then(bettingGames => bettingGames.get('length') === 1)})
    }).catch($.noop);
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

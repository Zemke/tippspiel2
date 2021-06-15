import Route from '@ember/routing/route';
import { computed } from '@ember/object';
import { inject } from '@ember/service';
import RSVP from 'rsvp';
import DS from 'ember-data';

export default Route.extend({
  intl: inject(),
  auth: inject(),
  resHandler: inject(),
  bettingGame: inject(),
  model() {
    const currentBettingGame = this.get('bettingGame.currentBettingGame');
    const bettingGames = currentBettingGame.then((currentBettingGame) => {
      const promise = this.get('auth.user').then((authenticatedUser) =>
        this.store.peekAll('betting-game')
      );
      return DS.PromiseArray.create({ promise: promise });
    });
    return RSVP.hash({
      currentBettingGame: currentBettingGame,
      bettingGames: bettingGames,
      isOnlyOneBettingGame: DS.PromiseObject.create({
        promise: bettingGames.then(
          (bettingGames) => bettingGames.get('length') === 1
        ),
      }),
    }).catch(() => {});
  },
  beforeModel() {
    iziToast.settings({ position: 'topRight' });

    let locale;
    try {
      const browserLocale = (
        navigator.languages && navigator.languages.length
          ? navigator.languages[0]
          : navigator.language
      ).toLowerCase();
      const rememberedLocale = localStorage.getItem('locale');
      locale = (rememberedLocale != null
        ? rememberedLocale
        : browserLocale
      ).startsWith('de')
        ? 'de'
        : 'en-us';
    } catch (e) {
      locale = 'en-us';
    }
    this.intl.setLocale(locale);
    localStorage.setItem('locale', locale);
  },
  actions: {
    error(error, transition) {
      console.error(error);

      let locKey;
      if (error.status === 404) {
        locKey = 'catchError.resourceNotFound';
      } else if (error.status === 503) {
        locKey = 'catchError.serviceUnavailable';
      } else if (error.status === 401 || error.status === 403) {
        locKey = 'catchError.accessDenied';
      } else {
        locKey = 'err.unknown';
      }

      this.resHandler.handleWithRouting(
        transition,
        this.transitionTo.bind(this),
        locKey
      );
    },
  },
});

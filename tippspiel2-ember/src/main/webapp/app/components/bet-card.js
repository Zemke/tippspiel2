import Component from '@ember/component';
import { computed } from '@ember/object';
import { later } from '@ember/runloop';
import { inject } from '@ember/service';

export default Component.extend({
  resHandler: inject(),
  actions: {
    submit(bet) {
      bet
        .save()
        .then((res) => this.resHandler.handleSuccess('success.betSaved'))
        .catch((err) => this.resHandler.handleError(err));
    },
  },
  disabled: computed('bet.fixture.date', function () {
    later(
      () => this.set('disabled', true),
      this.get('bet.fixture.date') - Date.now()
    );
  }).volatile(),
  live: computed('disabled', 'bet.fixture.status', function () {
    return (
      this.disabled &&
      (this.get('bet.fixture.status') === 'IN_PLAY' ||
        this.get('bet.fixture.status') === 'PAUSED')
    );
  }),
});

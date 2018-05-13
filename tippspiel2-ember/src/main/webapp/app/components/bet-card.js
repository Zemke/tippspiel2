import Component from '@ember/component';
import {computed} from '@ember/object';
import {later} from '@ember/runloop';

export default Component.extend({
  actions: {
    submit(bet) {
      bet.save()
        .then(res => iziToast.success({message: 'Bet saved.'}))
        .catch(res => iziToast.error({message: 'An unknown error occurred.'}));
    }
  },
  disabled: computed(function () {
    later(() => this.set('disabled', true), this.get('bet.fixture.date') - Date.now());
  }).volatile(),
  evaluatable: computed('bet.fixture.status', function () {
    return this.get('bet.fixture.status') === 'FINISHED';
  })
});

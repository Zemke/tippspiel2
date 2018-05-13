import Component from '@ember/component';
import {computed} from '@ember/object';
import {later} from '@ember/runloop';
import {inject} from '@ember/service';

export default Component.extend({
  resHandler: inject(),
  actions: {
    submit(bet) {
      bet.save()
        .then(res => this.get('resHandler').handleSuccess('Bet saved.'))
        .catch(this.get('resHandler').handleError);
    }
  },
  disabled: computed(function () {
    later(() => this.set('disabled', true), this.get('bet.fixture.date') - Date.now());
  }).volatile(),
  evaluatable: computed('bet.fixture.status', function () {
    return this.get('bet.fixture.status') === 'FINISHED';
  })
});

import Component from '@ember/component';
import {computed} from '@ember/object';

export default Component.extend({
  actions: {
    submit(bet) {
      bet.save()
        .then(res => iziToast.success({message: 'Bet saved.'}))
        .catch(res => iziToast.error({message: 'An unknown error occurred.'}));
    }
  },
  disabled: computed('bet.fixture.status', function () {
    const status = this.get('bet.fixture.status');
    return status !== 'SCHEDULED' && status !== 'TIMED';
  }),
  evaluatable: computed('bet.fixture.status', function () {
    return this.get('bet.fixture.status') === 'FINISHED';
  })
});

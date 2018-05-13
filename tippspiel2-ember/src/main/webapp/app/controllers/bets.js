import Controller from '@ember/controller';
import {computed} from '@ember/object';

export default Controller.extend({
  actions: {
    submit(bet) {
      bet.save()
        .then(res => iziToast.success({message: 'Bet saved.'}))
        .catch(res => iziToast.error({message: 'An unknown error occurred.'}));
    }
  },
  fixtureBetsDisabled: computed('model', function () {
    const isBettingDisabled = status => status !== 'SCHEDULED' && status !== 'TIMED';

    return this.get('model')
      .mapBy('fixture')
      .reduce((previousFixture, currentFixture, index, array) => {
        if (index === 1) {
          const obj = {};
          obj[previousFixture.get('id')] = isBettingDisabled(previousFixture.get('status'));
          obj[currentFixture.get('id')] = isBettingDisabled(currentFixture.get('status'));
          return obj;
        }

        previousFixture[currentFixture.get('id')] = isBettingDisabled(currentFixture.get('status'));
        return previousFixture;
      });
  })
});

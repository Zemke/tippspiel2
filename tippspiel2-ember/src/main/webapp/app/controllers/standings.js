import Controller from '@ember/controller';
import {computed} from '@ember/object';

export default Controller.extend({
  standingsAsTable: computed('model', function () {
    return this.get('model').toArray()
      .sort((s1, s2) => s1.get('points') < s2.get('points'))
      .sort((s1, s2) => s1.get('goalDifferenceBets') < s2.get('goalDifferenceBets'))
      .sort((s1, s2) => s1.get('winnerBets') < s2.get('winnerBets'))
      .sort((s1, s2) => s1.get('wrongBets') < s2.get('wrongBets'))
      .sort((s1, s2) => s1.get('missedBets') > s2.get('missedBets'))
      .map((value, index) => {
        value.set('position', index + 1);
        return value;
      });
  })
});

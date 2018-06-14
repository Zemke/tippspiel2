import Controller from '@ember/controller';
import {computed} from '@ember/object';

export default Controller.extend({
  standingsAsTable: computed('model.standings', 'model.championBets', 'model.competition', function () {
    function compare(s1, s2) {
      return  (s2.get('points') - s1.get('points')
               || s2.get('exactBets') - s1.get('exactBets')
               || s2.get('goalDifferenceBets') - s1.get('goalDifferenceBets')
               || s2.get('winnerBets') - s1.get('winnerBets')
               || s1.get('missedBets') - s2.get('missedBets')
               || s2.get('wrongBets') - s1.get('wrongBets'))
    }
    return this.get('model.standings').toArray()
      .sort((s1, s2) => compare(s1, s2))
      .map((value, index, arr) => {
        value.set('position', (index == 0 ? 1 :
                               arr[index-1].get('position') +
                               (compare(value, arr[index-1]) > 0 ? 1 : 0))
                 );

        const championBet = this.get('model.championBets').find(cB => cB.get('user.id') === value.get('user.id'));

        if (championBet != null) {
          value.set('championBet', championBet);
          value.set('hasCorrectChampionBet', this.get('model.competition.champion.id') === championBet.get('team.id'));
        }

        const isAuthenticatedUser = this.get('model.authenticatedUser.id') === value.get('user.id');
        value.set('isAuthenticatedUser', isAuthenticatedUser);

        value.set(
          'isAuthenticatedUserAndChampionBetAllowed',
          isAuthenticatedUser && this.get('model.competition.championBetAllowed') === true);

        return value;
      });
  }),
  championBetOfAuthenticatedUser: computed('model.authenticatedUser', 'model.championBets', function () {
    return this.get('model.championBets')
      .find(cB => cB.get('user.id') === this.get('model.authenticatedUser.id'))
  })
});

import Controller from '@ember/controller';
import {computed} from '@ember/object';

export default Controller.extend({
  compareStandings(s1, s2) {
    return (s2.get('points') - s1.get('points')
      || s2.get('exactBets') - s1.get('exactBets')
      || s2.get('goalDifferenceBets') - s1.get('goalDifferenceBets')
      || s2.get('winnerBets') - s1.get('winnerBets')
      || s1.get('missedBets') - s2.get('missedBets')
      || (s2.get('hasCorrectChampionBet')) - (s1.get('hasCorrectChampionBet') | 0))
  },
  standingsAsTable: computed('model.standings', 'model.championBets', 'model.competition', function () {
    return this.get('model.standings').toArray()
      .map(s => {
        const championBet = this.get('model.championBets').find(cB => cB.get('user.id') === s.get('user.id'));

        if (championBet != null) {
          s.set('championBet', championBet);
          s.set('hasCorrectChampionBet', this.get('model.competition.champion.id') === championBet.get('team.id'));
        }

        return s;
      })
      .sort((s1, s2) => this.compareStandings(s1, s2))
      .map((value, index, arr) => {
        value.set('position', (index === 0 ? 1 : (this.compareStandings(value, arr[index - 1]) > 0 ? index + 1 : null)));

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

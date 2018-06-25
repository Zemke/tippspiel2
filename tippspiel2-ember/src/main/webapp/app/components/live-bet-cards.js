import Component from '@ember/component';
import {computed} from '@ember/object';
import {inject} from '@ember/service';

export default Component.extend({
  classNames: ['columns', 'is-multiline'],
  bet: inject('bet'),
  store: inject('store'),
  eventSourcePool: inject('eventSourcePool'),
  init() {
    this._super(...arguments);

    window.setTimeout(() => {
      const shuffleInstance = new Shuffle(this.get('element'));

      this.get('eventSourcePool').acquireSourceFixtureById(this.get('bets').objectAt(0).get('fixture.id')).onmessage = e => {
        const fixtureUpdate = JSON.parse(e.data);
        const fixtureFromStore = this.get('store').peekRecord('fixture', fixtureUpdate.id);
        fixtureFromStore.set('goalsHomeTeam', fixtureUpdate.goalsHomeTeam);
        fixtureFromStore.set('goalsAwayTeam', fixtureUpdate.goalsAwayTeam);

        shuffleInstance.sort({
          compare: (a, b) => {
            let evalB = this.get('bet').evaluate(
              fixtureUpdate.goalsHomeTeam, fixtureUpdate.goalsAwayTeam,
              parseInt(b.element.getAttribute('data-bet-goals-home-team')),
              parseInt(b.element.getAttribute('data-bet-goals-away-team')));
            if (evalB === null) evalB = -1;

            let evalA = this.get('bet').evaluate(
              fixtureUpdate.goalsHomeTeam, fixtureUpdate.goalsAwayTeam,
              parseInt(a.element.getAttribute('data-bet-goals-home-team')),
              parseInt(a.element.getAttribute('data-bet-goals-away-team')));
            if (evalA === null) evalA = -1;

            return evalB - evalA;
          }
        });
      };
    }, 5000)
  }
});

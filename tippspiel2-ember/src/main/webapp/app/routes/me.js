import Route from '@ember/routing/route';
import RSVP from 'rsvp';
import { inject } from '@ember/service';

export default Route.extend({
  bettingGame: inject(),
  intl: inject(),
  model() {
    const competitions = this.store.findAll('competition');
    const currentCompetition = competitions.then((competitions) =>
      competitions.findBy('current', true)
    );
    const teams = currentCompetition.then((currentCompetition) => {
      if (currentCompetition == null) {
        return null;
      }

      return this.store
        .query('team', { competition: currentCompetition.get('id') })
        .then((teams) =>
          teams
            .toArray()
            .sort((a, b) =>
              this.intl
                .t(`team.name.${a.id}`)
                .localeCompare(this.intl.t(`team.name.${b.id}`))
            )
        );
    });

    return RSVP.hash({ competitions, currentCompetition, teams });
  },
});

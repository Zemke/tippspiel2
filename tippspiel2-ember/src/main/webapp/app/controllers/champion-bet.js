import Controller from '@ember/controller';
import { inject } from '@ember/service';

export default Controller.extend({
  resHandler: inject(),
  actions: {
    selectTeam(teamId) {
      this.set(
        'model.championBet.team',
        this.get('model.teams').find((t) => t.id === teamId)
      );
    },
    submit() {
      this.get('model.championBet')
        .save()
        .then((res) => {
          this.resHandler.handleSuccess('success.betSaved');
          this.transitionToRoute('standings');
        })
        .catch((err) => this.resHandler.handleError(err));
    },
  },
});

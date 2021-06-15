import Controller from '@ember/controller';
import { inject } from '@ember/service';

export default Controller.extend({
  auth: inject(),
  resHandler: inject(),
  intl: inject(),
  actions: {
    updateCurrentCompetition(newCurrentCompetition) {
      newCurrentCompetition.set('current', true);
      return newCurrentCompetition
        .save()
        .then(() => this.resHandler.handleSuccess('success.competitionSaved'))
        .catch((err) => this.resHandler.handleError(err));
    },
    manuallySetChampion(championTeam) {
      const teamName = this.intl.t(`team.name.${championTeam.id}`);
      if (!confirm(this.intl.t('me.manualChampion.confirm', { teamName }))) {
        return;
      }

      this.set('model.currentCompetition.champion', championTeam);
      this.get('model.currentCompetition')
        .save()
        .then(() => this.resHandler.handleSuccess('me.manualChampion.success'))
        .catch((err) => this.resHandler.handleError(err));
    },
  },
});

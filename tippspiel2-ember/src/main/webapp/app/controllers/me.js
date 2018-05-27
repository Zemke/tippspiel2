import Controller from '@ember/controller';
import {inject} from '@ember/service';

export default Controller.extend({
  auth: inject(),
  resHandler: inject(),
  actions: {
    updateCurrentCompetition(newCurrentCompetition) {
      newCurrentCompetition.set('current', true);
      return newCurrentCompetition.save()
        .then(() => this.get('resHandler').handleSuccess('success.competitionSaved'))
        .catch(err => this.get('resHandler').handleError(err));
    }
  }
});

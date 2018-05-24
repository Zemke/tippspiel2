import Controller from '@ember/controller';
import {inject} from '@ember/service';

export default Controller.extend({
  auth: inject(),
  resHandler: inject(),
  actions: {
    updateCurrentCompetition(newCurrentCompetition) {
      newCurrentCompetition.set('current', true);
      return newCurrentCompetition.save()
        .then(() => this.get('resHandler').handleSuccess("Competition has been updated."))
        .catch(this.get('resHandler').handleError);
    }
  }
});

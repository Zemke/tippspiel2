import Controller from '@ember/controller';
import {inject} from '@ember/service';

export default Controller.extend({
  auth: inject(),
  bettingGame: inject(),
  resHandler: inject(),
  actions: {
    updateCurrentCompetition() {
      this.get('bettingGame.currentBettingGame').then(currentBettingGame =>
        this.get('store').peekRecord('competition', currentBettingGame.get('competition.id')).save()
          .then(() => this.get('resHandler').handleSuccess("Competition has been updated."))
          .catch(this.get('resHandler').handleError));
    }
  }
});

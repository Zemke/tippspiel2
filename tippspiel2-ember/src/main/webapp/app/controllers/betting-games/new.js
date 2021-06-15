import Controller from '@ember/controller';
import { inject } from '@ember/service';

export default Controller.extend({
  resHandler: inject(),
  actions: {
    submit() {
      this.model.bettingGame
        .save()
        .then((res) => {
          this.resHandler.handleSuccess('success.bettingGameCreated');
          this.transitionToRoute('betting-games.details', res);
        })
        .catch((err) => this.resHandler.handleError(err));
    },
  },
});

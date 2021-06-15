import Controller from '@ember/controller';
import { inject } from '@ember/service';

export default Controller.extend({
  resHandler: inject(),
  actions: {
    submit() {
      this.model
        .save()
        .then((res) => {
          this.resHandler.handleSuccess('success.competitionCreated');
          this.transitionToRoute('me');
        })
        .catch((err) => this.resHandler.handleError(err));
    },
  },
});

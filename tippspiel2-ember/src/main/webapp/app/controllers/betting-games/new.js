import Controller from '@ember/controller';
import {inject} from '@ember/service';

export default Controller.extend({
  resHandler: inject(),
  actions: {
    submit() {
      this.model.bettingGame.save()
        .then(res => {
          this.get('resHandler').handleSuccess('success.bettingGameCreated');
          this.transitionToRoute('me');
        })
        .catch(err => this.get('resHandler').handleError(err));
    }
  }
});

import Controller from '@ember/controller';
import {inject} from '@ember/service';

export default Controller.extend({
  resHandler: inject(),
  actions: {
    submit() {
      this.model.bettingGame.save()
        .then(res => {
          this.get('resHandler').handleSuccess('The betting game has been created.');
          this.transitionToRoute('me');
        })
        .catch(this.get('resHandler').handleError);
    }
  }
});

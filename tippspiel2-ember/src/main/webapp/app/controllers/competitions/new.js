import Controller from '@ember/controller';
import {inject} from '@ember/service';

export default Controller.extend({
  resHandler: inject(),
  actions: {
    submit() {
      this.model.save()
        .then(res => {
          this.get('resHandler').handleSuccess('The community has been created.');
          this.transitionToRoute('me');
        })
        .catch(this.get('resHandler').handleError);
    }
  }
});

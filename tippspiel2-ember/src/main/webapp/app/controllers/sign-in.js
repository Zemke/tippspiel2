import Controller from '@ember/controller';
import { inject } from '@ember/service';

export default Controller.extend({
  auth: inject(),
  resHandler: inject(),
  actions: {
    submit() {
      this.model
        .save()
        .then((res) => {
          this.get('auth').storeToken(res.get('id'));
          window.location.href = '/';
        })
        .catch((err) => this.get('resHandler').handleError(err));
    },
  },
});

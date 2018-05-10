import Controller from '@ember/controller';
import {inject} from '@ember/service';

export default Controller.extend({
  auth: inject(),
  actions: {
    submit() {
      this.model.save()
        .then(res => {
          this.get('auth').storeToken(res.get('token'));
          window.location.href = '/';
        })
        .catch(res =>
          iziToast.error({message: 'An unknown error occurred.'}))
    }
  }
});

import Controller from '@ember/controller';
import {inject} from '@ember/service';

export default Controller.extend({
  auth: inject(),
  actions: {
    submit() {
      this.model.save()
        .then(res => {
          this.get('auth').storeToken(res.get('id'));
          window.location.href = '/';
        })
        .catch(res => {
          const message = res.status === 401
            ? 'Invalid credentials.'
            : 'An unknown error occurred.';
          iziToast.error({message});
        })
    }
  }
});

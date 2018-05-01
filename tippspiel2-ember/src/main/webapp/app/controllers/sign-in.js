import Controller from '@ember/controller';
import {inject} from '@ember/service';

export default Controller.extend({
  auth: inject(),
  actions: {
    submit() {
      this.model.save()
        .then(res => {
          iziToast.success({message: 'You’re signed in.'});
          this.get('auth').signIn(res.get('id'));
          this.transitionToRoute('index');
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

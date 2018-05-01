import Controller from '@ember/controller';
import {inject} from '@ember/service';

export default Controller.extend({
  auth: inject(),
  actions: {
    submit() {
      this.model.save()
        .then(res => {
          iziToast.success({message: 'You’ve successfully signed up.'});
          this.get('auth').signIn(res.get('token'));
          this.transitionToRoute('index');
        })
        .catch(res =>
          iziToast.error({message: 'An unknown error occurred.'}))
    }
  }
});

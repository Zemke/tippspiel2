import Controller from '@ember/controller';
import {inject} from '@ember/service';
import {computed} from '@ember/object';

export default Controller.extend({
  auth: inject(),
  resHandler: inject(),
  queryParams: {invitationToken: 'invitation-token'},
  invitationToken: null,
  invitationTokenPreSet: computed('invitationToken', function () {
    return this.get('invitationToken');
  }),
  actions: {
    submit() {
      this.model.save()
        .then(res => {
          this.get('auth').storeToken(res.get('token'));
          window.location.href = '/';
        })
        .catch(this.get('resHandler').handleError)
    }
  }
});

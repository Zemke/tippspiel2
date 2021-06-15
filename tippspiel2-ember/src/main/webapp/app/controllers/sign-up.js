import Controller from '@ember/controller';
import { inject } from '@ember/service';
import { computed } from '@ember/object';

export default Controller.extend({
  auth: inject(),
  resHandler: inject(),
  queryParams: { invitationToken: 'invitation-token' },
  invitationToken: null,
  invitationTokenPreSet: computed.reads('invitationToken'),
  actions: {
    submit() {
      this.model
        .save()
        .then((res) => {
          this.auth.storeToken(res.get('token'));
          window.location.href = '/';
        })
        .catch((err) => this.resHandler.handleError(err));
    },
  },
});

import Controller from '@ember/controller';
import {computed} from '@ember/object';
import {inject} from '@ember/service';

export default Controller.extend({
  resHandler: inject(),
  actions: {
    submit() {
      this.model.community.save()
        .then(res => {
          this.get('resHandler').handleSuccess('The community has been created.');
          this.transitionToRoute('me');
        })
        .catch(this.get('resHandler').handleError);
    },
    addUser(event) {
      this.get('model.community.users').pushObject(
        this.get('model.users').find(u => u.id === event.target.value));
      event.target.value = '';
    },
    removeUser(user) {
      this.get('model.community.users').removeObject(user);
    }
  },
  usersInCommunity: computed('model.community.users.[]', function () {
    return this.get('model.community.users').sortBy('lastName', 'firstName')
  }),
  usersNotInCommunity: computed('model.community.users.[]', function () {
    const usersInCommunity = this.get('model.community.users').toArray();
    return this.get('model.users').filter(u => usersInCommunity.indexOf(u) === -1);
  })
});

import Controller from '@ember/controller';
import {computed} from '@ember/object';

export default Controller.extend({
  actions: {
    submit() {
      this.model.community.save()
        .then(res => {
          iziToast.success({message: 'The community has been created.'});
          this.transitionToRoute('me');
        })
        .catch(res => {
          iziToast.error({message: 'An unknown error occurred.'});
        });
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

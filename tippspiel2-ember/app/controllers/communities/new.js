import Controller from '@ember/controller';

export default Controller.extend({
  actions: {
    submit() {
      this.model.community.save()
        .then(res => {
          iziToast.success({message: 'The community has been created.'});
          this.transitionToRoute('me');
        })
        .catch(res => {
          debugger;
          // TODO Assertion Failed: You can no longer pass a modelClass as the first argument to store._buildInternalModel. Pass modelName instead.
          iziToast.error({message: 'An unknown error occurred.'});
        });
    },
    addUser(userId) {
      this.get('model.community.users').pushObject(this.get('model.users').find(u => u.id === userId));
    },
    removeUser(user) {
      this.get('model.community.users').removeObject(user);
    }
  }
});

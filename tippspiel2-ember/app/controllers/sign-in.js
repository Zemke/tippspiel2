import Controller from '@ember/controller';

export default Controller.extend({
  actions: {
    submit() {
      this.model.save()
        .then(res => {
          iziToast.success({message: 'You’re signed in.'});
          this.transitionToRoute('index');
        })
        .catch(reason => {
          iziToast.error({message: 'Apparently something went wrong.'});
          console.log(reason);
          // TODO Design REST errors the way Ember can handle them.
        })
    }
  }
});

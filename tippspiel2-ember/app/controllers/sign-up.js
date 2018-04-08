import Controller from '@ember/controller';

export default Controller.extend({
  actions: {
    submit() {
      this.model.save()
        .then((res, x, y, z) => {
          iziToast.success({message: 'You’ve successfully signed up.'});
        })
        .catch((reason, x, y, z) => {
          iziToast.success({message: 'Apparently something went wrong.'});
          console.log(reason);
          // TODO Design REST errors the way Ember can handle them.
        })
    }
  }
});

import Controller from '@ember/controller';

export default Controller.extend({
  actions: {
    submit() {
      this.model.save()
        .then(() => iziToast.success({message: 'You’ve successfully signed up.'}))
        .catch(res => iziToast.error({message: 'An unknown error occurred.'}))
    }
  }
});

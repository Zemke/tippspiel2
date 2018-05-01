import Controller from '@ember/controller';

export default Controller.extend({
  actions: {
    submit() {
      this.model.save()
        .then(res => {
          iziToast.success({message: 'The community has been created.'});
          this.transitionToRoute('me');
        })
        .catch(res => {
          iziToast.error({message: 'An unknown error occurred.'});
        });
    }
  }
});

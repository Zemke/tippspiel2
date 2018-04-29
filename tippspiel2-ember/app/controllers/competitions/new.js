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
           // TODO "Assertion Failed: 'competition:1212' was saved to the server, but the response returned the new id '467'. The store cannot assign a new id to a record that already has an id."
          iziToast.error({message: 'An unknown error occurred.'});
        });
    }
  }
});

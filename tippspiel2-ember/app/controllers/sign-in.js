import Controller from '@ember/controller';

export default Controller.extend({
  actions: {
    submit() {
      this.model.save()
        .then(res => {
          iziToast.success({message: 'You’re signed in.'});
          this.transitionToRoute('index');
        })
        .catch(res => {
          const message = res.status === 401
            ? 'Invalid credentials.'
            : 'An unknown error occurred.';
          iziToast.error({message});
        })
    }
  }
});

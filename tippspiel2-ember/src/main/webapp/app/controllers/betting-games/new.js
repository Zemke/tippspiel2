import Controller from '@ember/controller';

export default Controller.extend({
  actions: {
    submit() {
      this.model.bettingGame.save()
        .then(res => {
          iziToast.success({message: 'The betting game has been created.'});
          this.transitionToRoute('me');
        })
        .catch(res => {
          iziToast.error({message: 'An unknown error occurred.'});
        });
    },
    setCommunity(selectedCommunityId) {
      this.set(
        'model.bettingGame.community',
        this.get('model.communities').find(u => u.id === selectedCommunityId));
    }
  }
});

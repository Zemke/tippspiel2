import Controller from '@ember/controller';

export default Controller.extend({
  actions: {
    submit(bet) {
      bet.save()
        .then(res => iziToast.success({message: 'Bet saved.'}))
        .catch(res => iziToast.error({message: 'An unknown error occurred.'}));
    }
  }
});

import Service from '@ember/service';

export default Service.extend({
  handleSuccess(message) {
    iziToast.success({message});
  },
  handleError(res) {
    iziToast.error({message: res.message});
  },
  handleWithRouting(transition, transitionTo, message) {
    if (transition.sequence === 0) {
      alert(message);
      transitionTo('application');
    } else {
      this.handleError({message});
      transition.abort();
    }
  }
});

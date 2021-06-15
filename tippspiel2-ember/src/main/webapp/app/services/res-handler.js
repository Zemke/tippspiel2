import Service, { inject } from '@ember/service';

export default Service.extend({
  intl: inject(),
  handleSuccess(locKey) {
    iziToast.success({ message: this.intl.t(locKey) });
  },
  handleError(res) {
    const locKey = res.locKey || 'err.unknown';
    iziToast.error({ message: this.intl.t(locKey) });
  },
  handleWithRouting(transition, transitionTo, locKey) {
    const message = this.intl.t(locKey);
    if (transition.sequence === 0) {
      alert(message);
      transitionTo('application');
    } else {
      this.handleError({ message });
      transition.abort();
    }
  },
});

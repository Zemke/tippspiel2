import Service from '@ember/service';

export default Service.extend({
  handleSuccess(message) {
    iziToast.success(message);
  },
  handleError(res) {
    iziToast.error({message: res.message});
  }
});

import RESTAdapter from 'ember-data/adapters/rest';
import {computed} from '@ember/object';

export default RESTAdapter.extend({
  host: 'http://0.0.0.0:8080',
  namespace: 'api',
  handleResponse(status, headers, payload, requestData) {
    return status >= 200 && status <= 299
      ? this._super(...arguments)
      : payload;
  },
  headers: computed(function () {
    const authTokenFromLocalStorage = localStorage.getItem('auth-token');
    return authTokenFromLocalStorage != null
      ? {Authorization: 'Bearer ' + authTokenFromLocalStorage}
      : {};
  }).volatile()
});

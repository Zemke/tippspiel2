import RESTAdapter from 'ember-data/adapters/rest';
import {computed} from '@ember/object';
import {inject} from '@ember/service';

export default RESTAdapter.extend({
  auth: inject(),
  host: 'http://0.0.0.0:8080',
  namespace: 'api',
  handleResponse(status, headers, payload, requestData) {
    return status >= 200 && status <= 299
      ? this._super(...arguments)
      : payload;
  },
  headers: computed(function () {
    return this.get('auth').token != null
      ? {Authorization: 'Bearer ' + this.get('auth').token}
      : {};
  }).volatile()
});

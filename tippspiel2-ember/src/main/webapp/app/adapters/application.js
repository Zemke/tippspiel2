import RESTAdapter from 'ember-data/adapters/rest';
import {computed} from '@ember/object';
import {inject} from '@ember/service';
import {decamelize} from '@ember/string';
import {pluralize} from 'ember-inflector';

export default RESTAdapter.extend({
  auth: inject(),
  host: 'http://0.0.0.0:8080',
  namespace: 'api',
  pathForType(modelName) {
    return decamelize(pluralize(modelName));
  },
  handleResponse(status, headers, payload, requestData) {
    return status >= 200 && status <= 299
      ? this._super(...arguments)
      : payload;
  },
  headers: computed(function () {
    const token = this.get('auth').getToken();
    return token != null
      ? {Authorization: 'Bearer ' + token}
      : {};
  }).volatile()
});

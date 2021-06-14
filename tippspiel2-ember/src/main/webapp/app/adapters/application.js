import RESTAdapter from 'ember-data/adapters/rest';
import {computed} from '@ember/object';
import {inject} from '@ember/service';
import {decamelize} from '@ember/string';
import {pluralize} from 'ember-inflector';
import config from '../config/environment';

// const host = config.environment === "development" ? "http://localhost:8080" : null; TODO GH-91

export default RESTAdapter.extend({
  auth: inject(),
  namespace: 'api',
  pathForType(modelName) {
    return decamelize(pluralize(modelName));
  },
  handleResponse(status, headers, payload, requestData) {
    if (status === 500) {
      return {status, locKey: payload.locKey};
    } else if (status === 401 || status === 403) {
      return {status, locKey: payload.locKey};
    } else if (status === 400 || status === 404) {
      return {status, locKey: payload.locKey};
    } else {
      return this._super(...arguments);
    }
  },
  headers: computed(function () {
    const token = this.get('auth').getToken();
    return token != null
      ? {Authorization: 'Bearer ' + token}
      : {};
  }).volatile()
});

import RESTAdapter from 'ember-data/adapters/rest';

export default RESTAdapter.extend({
  host: 'http://0.0.0.0:8080',
  namespace: 'api'
});

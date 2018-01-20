import DS from 'ember-data';
import ENV from 'tippspiel2-ember/config/environment'

export default DS.RESTAdapter.extend({
  namespace: 'api',
  host: ENV.APP.host
});

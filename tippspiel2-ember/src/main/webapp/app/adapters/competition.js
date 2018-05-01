import $ from 'jquery';
import ApplicationRESTAdapter from './application';

export default ApplicationRESTAdapter.extend({
  queryRecord(store, type, query) {
    return $.getJSON(`${this.host}/${this.namespace}/competitions/current`)
  }
});

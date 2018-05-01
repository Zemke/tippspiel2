import {decamelize} from '@ember/string';
import ApplicationRESTAdapter from './application';

export default ApplicationRESTAdapter.extend({
  pathForType: decamelize
});

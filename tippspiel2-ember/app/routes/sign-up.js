import Route from '@ember/routing/route';
import {getOwner} from '@ember/application';

export default Route.extend({
  model() {
    return {firstName: 'Joe'};
  }
});

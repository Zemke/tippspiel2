import Model from '@ember-data/model';
import { attr } from '@ember-data/model';

export default Model.extend({
  hello: attr('string'),
});

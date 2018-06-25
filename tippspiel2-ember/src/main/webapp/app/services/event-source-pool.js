import Service, {inject} from '@ember/service';
import {computed} from '@ember/object';

export default Service.extend({
  store: inject(),
  init() {
    this._super(...arguments);
    this.set('url', `${this.get('store').adapterFor('application').host}/${this.get('store').adapterFor('application').namespace}`);
  },
  acquireSourceFixtureById(fixtureId) {
    return new EventSource(`${this.get('url')}/stream/fixtures/${fixtureId}`);
  },
  acquireSourceFixtures() {
    return new EventSource(`${this.get('url')}/stream/fixtures?status=IN_PLAY`);
  }
});

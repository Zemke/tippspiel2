import Route from '@ember/routing/route';

export default Route.extend({
  model() {
    this.store.adapterFor('fixture').set('namespace', 'api/competitions/467');
    return this.get('store').findAll('fixture');
  },
  afterModel() {
    this.store.adapterFor('fixture').set('namespace', 'api');
  }
});

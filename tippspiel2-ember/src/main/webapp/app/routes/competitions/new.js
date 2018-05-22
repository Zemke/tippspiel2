import Route from '@ember/routing/route';

export default Route.extend({
  model() {
    return this.store.createRecord('competition');
  },
  beforeModel(transition) {
    return this.get('auth.isAdmin').then(isAdmin =>
      !isAdmin && this.get('resHandler').handleWithRouting(transition, this.transitionTo.bind(this), "Access denied"))
  }
});

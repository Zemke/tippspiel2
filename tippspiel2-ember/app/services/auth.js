import Service from '@ember/service';

export default Service.extend({
  authenticated: null,
  init() {
    this._super(...arguments);
    this.set('authenticated', localStorage.getItem('auth-token') != null);
  },
  signOut() {
    this.set('authenticated', false);
    localStorage.removeItem('auth-token');
  },
  signIn(authToken) {
    localStorage.setItem('auth-token', authToken);
    this.set('authenticated', true);
  }
});

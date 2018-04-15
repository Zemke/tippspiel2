import Service from '@ember/service';

export default Service.extend({
  authenticated: null,
  user: null,
  token: null,
  init() {
    this._super(...arguments);

    const authToken = localStorage.getItem('auth-token');
    if (authToken != null) {
      this.set('token', authToken);
      this.set('authenticated', true);
      this.set('user', this.get('parseTokenPayload')(authToken));
    }
  },
  signIn(authToken) {
    localStorage.setItem('auth-token', authToken);
    this.set('token', authToken);
    this.set('authenticated', true);
    this.set('user', this.get('parseTokenPayload')(authToken));
  },
  signOut() {
    localStorage.removeItem('auth-token');
    this.set('token', null);
    this.set('authenticated', false);
    this.set('user', null);
  },
  parseTokenPayload(authToken) {
    return JSON.parse(atob(authToken.split('.')[1]));
  }
});

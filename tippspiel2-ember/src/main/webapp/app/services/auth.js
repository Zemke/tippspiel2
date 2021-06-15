import Service, { inject } from '@ember/service';
import DS from 'ember-data';
import { computed } from '@ember/object';

export default Service.extend({
  store: inject(),
  user: computed(function () {
    return DS.PromiseObject.create({
      promise: new Promise((resolve, reject) => {
        const token = this.getToken();
        if (token == null)
          return reject({ status: 401, message: 'Access denied.' });

        this.get('store')
          .findRecord('auth', token)
          .then((res) => {
            this.storeToken(res.id);
            resolve(this.parseTokenPayload(res.id));
          })
          .catch((err) => {
            this.wipeToken();
            reject(err);
          });
      }),
    });
  }),
  isAdmin: computed('user', function () {
    const promise = this.get('user').then(
      (authenticatedUser) => authenticatedUser.roles.indexOf('ADMIN') !== -1
    );
    return DS.PromiseObject.create({ promise: promise });
  }),
  storeToken(token) {
    localStorage.setItem('auth-token', token);
  },
  wipeToken() {
    localStorage.removeItem('auth-token');
  },
  getToken() {
    return localStorage.getItem('auth-token');
  },
  parseTokenPayload(token) {
    const decoded = decodeURIComponent(
      Array.prototype.map
        .call(
          atob(token.split('.')[1]),
          (c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
        )
        .join('')
    );
    const payload = JSON.parse(decoded);
    payload.id = payload.id.toString();
    return payload;
  },
});

import Service, {inject} from '@ember/service';
import DS from 'ember-data';
import {computed} from '@ember/object';

export default Service.extend({
  store: inject(),
  user: computed(function () {
    return DS.PromiseObject.create({
      promise: new Promise((resolve, reject) => {
        const token = this.getToken();
        if (token == null) return reject();

        this.get('store').findRecord('auth', token)
          .then(res => {
            this.storeToken(res.id);
            resolve(this.parseTokenPayload(res.id));
          })
          .catch(() => {
            this.wipeToken();
            reject();
          });
      })

    });
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
    const payload = JSON.parse(atob(token.split('.')[1]));
    payload.id = payload.id.toString();
    return payload;
  }
});

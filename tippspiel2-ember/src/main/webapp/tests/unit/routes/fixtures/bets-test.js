import { module, test } from 'qunit';
import { setupTest } from 'ember-qunit';

module('Unit | Route | fixtures/bets', function (hooks) {
  setupTest(hooks);

  test('it exists', function (assert) {
    let route = this.owner.lookup('route:fixtures/bets');
    assert.ok(route);
  });
});
